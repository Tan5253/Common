package com.like.common.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
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
                    "releaseVersion='" + releaseVersion + '\'' +
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

        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mPhoneStatus.imei = tm.getDeviceId();
            mPhoneStatus.phoneNumber = tm.getLine1Number();
            return;
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
