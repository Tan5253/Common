package com.like.common.util;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.like.logger.Logger;

/**
 * 手机相关的工具类
 * <p>
 * Manifest.permission.READ_PHONE_STATE
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
         * 本机电话号码
         */
        public String phoneNumber;
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
            return "PhoneStatus [phoneNumber=" + phoneNumber + ", model=" + model + ", sdkVersion=" + sdkVersion + ", imei=" + imei + ", screenWidth="
                    + screenWidth + ", screenWidthDpi=" + screenWidthDpi + ", screenHeight=" + screenHeight + ", screenHeightDpi=" + screenHeightDpi
                    + ", density=" + density + ", densityDpi=" + densityDpi + "]";
        }

    }

    /**
     * 获取手机相关的状态信息 需要请求电话状态信息的权限 READ_PHONE_STATE
     */
    public void initPhoneStatus() {
        mPhoneStatus = new PhoneStatus();
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneStatus.imei = tm.getDeviceId();
            mPhoneStatus.model = Build.MODEL;
            mPhoneStatus.phoneNumber = tm.getLine1Number();
            mPhoneStatus.sdkVersion = Build.VERSION.SDK_INT;

            DisplayMetrics metric = new DisplayMetrics();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
            mPhoneStatus.screenWidth = metric.widthPixels;
            mPhoneStatus.screenWidthDpi = DimensionUtils.px2dp(mContext, metric.widthPixels);
            mPhoneStatus.screenHeight = metric.heightPixels;
            mPhoneStatus.screenHeightDpi = DimensionUtils.px2dp(mContext, metric.heightPixels);
            mPhoneStatus.density = metric.density;
            mPhoneStatus.densityDpi = metric.densityDpi;
            Logger.i(mPhoneStatus);
        } catch (Exception e) {
            mPhoneStatus = null;
            Logger.e("获得手机相关的状态信息失败 " + e.toString());
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return pm.isInteractive();
        } else {
            // noinspection all
            return pm.isScreenOn();
        }
    }

}
