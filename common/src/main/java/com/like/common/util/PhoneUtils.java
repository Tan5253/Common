package com.like.common.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.like.logger.Logger;

/**
 * 手机相关的工具类
 * <p>
 * android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.READ_SMS
 */
public class PhoneUtils {
    public PhoneStatus mPhoneStatus;

    private Context mContext;
    private volatile static PhoneUtils sInstance = null;

    public static PhoneUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PhoneUtils.class) {
                if (sInstance == null) {
                    sInstance = new PhoneUtils(context);
                }
            }
        }
        return sInstance;
    }

    private PhoneUtils(Context context) {
        mContext = context.getApplicationContext();
        initPhoneStatus();
    }

    /**
     * 手机相关的状态信息
     */
    public static class PhoneStatus {
        /**
         * AndroidId
         * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来。不需要权限，平板设备通用。获取成功率也较高，缺点是设备恢复出厂设置会重置。另外就是某些厂商的低版本系统会有bug，返回的都是相同的AndroidId。
         */
        public String androidId;
        /**
         * serialNumber
         * Android系统2.3版本以上可以通过下面的方法得到Serial Number，且非手机设备也可以通过该接口获取。不需要权限，通用性也较高，但我测试发现红米手机返回的是 0123456789ABCDEF 明显是一个顺序的非随机字符串。也不一定靠谱。
         */
        public String serialNumber;
        /**
         * MAC地址
         */
        public String mac;
        /**
         * android系统版本
         */
        public String releaseVersion;
        /**
         * 本机电话号码
         */
        public String phoneNumber;
        /**
         * 手机品牌
         */
        public String brand;
        /**
         * 手机型号
         */
        public String model;
        /**
         * SDK版本号
         */
        public int sdkVersion;
        /**
         * IMEI号
         * getDeviceId()需要android.permission.READ_PHONE_STATE权限，它在6.0+系统中是需要动态申请的。如果需求要求App启动时上报设备标识符的话，那么第一会影响初始化速度，第二还有可能被用户拒绝授权。
         * android系统碎片化严重，有的手机可能拿不到DeviceId，会返回null或者000000。
         * 这个方法是只对有电话功能的设备有效的，在pad上不起作用。 可以看下方法注释
         */
        public String imei;
        /**
         * 屏幕宽度（像素）
         */
        public int screenWidth;
        /**
         * 屏幕宽度（DP）
         */
        public int screenWidthDpi;
        /**
         * 屏幕高度（像素）
         */
        public int screenHeight;
        /**
         * 屏幕高度（DP）
         */
        public int screenHeightDpi;
        /**
         * 屏幕密度（0.75 / 1.0 / 1.5）
         */
        public float density;
        /**
         * 屏幕密度DPI（120 / 160 / 240）
         */
        public int densityDpi;

        @Override
        public String toString() {
            return "PhoneStatus{" +
                    "androidId='" + androidId + '\'' +
                    ", serialNumber='" + serialNumber + '\'' +
                    ", mac='" + mac + '\'' +
                    ", releaseVersion='" + releaseVersion + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", brand='" + brand + '\'' +
                    ", model='" + model + '\'' +
                    ", sdkVersion=" + sdkVersion +
                    ", imei='" + imei + '\'' +
                    ", screenWidth=" + screenWidth +
                    ", screenWidthDpi=" + screenWidthDpi +
                    ", screenHeight=" + screenHeight +
                    ", screenHeightDpi=" + screenHeightDpi +
                    ", density=" + density +
                    ", densityDpi=" + densityDpi +
                    '}';
        }
    }

    /**
     * 获取手机相关的状态信息 需要请求电话状态信息的权限 READ_PHONE_STATE
     */
    private void initPhoneStatus() {
        mPhoneStatus = new PhoneStatus();
        mPhoneStatus.releaseVersion = Build.VERSION.RELEASE;
        mPhoneStatus.brand = Build.BRAND;
        mPhoneStatus.model = Build.MODEL;
        mPhoneStatus.sdkVersion = Build.VERSION.SDK_INT;
        mPhoneStatus.serialNumber = Build.SERIAL;
        mPhoneStatus.androidId = Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mPhoneStatus.imei = tm.getDeviceId();
            mPhoneStatus.phoneNumber = tm.getLine1Number();
            return;
        }

        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                mPhoneStatus.mac = wifiInfo.getMacAddress();
            }
        }

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics metric = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metric);
            mPhoneStatus.screenWidth = metric.widthPixels;
            mPhoneStatus.screenWidthDpi = DimensionUtils.px2dp(mContext, metric.widthPixels);
            mPhoneStatus.screenHeight = metric.heightPixels;
            mPhoneStatus.screenHeightDpi = DimensionUtils.px2dp(mContext, metric.heightPixels);
            mPhoneStatus.density = metric.density;
            mPhoneStatus.densityDpi = metric.densityDpi;
        }
        Logger.i(mPhoneStatus);
    }

    /**
     * 检测屏幕是否开启
     *
     * @param context 上下文
     * @return 是否屏幕开启
     */
    public static boolean isScreenOn(Context context) {
        Context appContext = context.getApplicationContext();
        PowerManager pm = (PowerManager) appContext.getSystemService(Context.POWER_SERVICE);

        if (pm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                return pm.isInteractive();
            } else {
                // noinspection all
                return pm.isScreenOn();
            }
        }
        return false;
    }

}
