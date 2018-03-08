package com.like.common.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.like.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * 手机相关的工具类
 * <p>
 * android.Manifest.permission.READ_PHONE_STATE，android.Manifest.permission.READ_SMS，android.Manifest.permission.READ_PHONE_NUMBERS
 */
public class PhoneUtils {
    private final static String DEFAULT_FILE_NAME = ".phoneutils_device_id";
    private final static String FILE_DOWNLOADS = StorageUtils.ExternalStorageHelper.getPublicDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + DEFAULT_FILE_NAME;
    private final static String FILE_DCIM = StorageUtils.ExternalStorageHelper.getPublicDir(Environment.DIRECTORY_DCIM) + File.separator + DEFAULT_FILE_NAME;
    private final static String KEY_UUID = "PHONEUTILS_KEY_UUID";
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
         * deviceId
         * getDeviceId()需要android.permission.READ_PHONE_STATE权限，它在6.0+系统中是需要动态申请的。如果需求要求App启动时上报设备标识符的话，那么第一会影响初始化速度，第二还有可能被用户拒绝授权。
         * android系统碎片化严重，有的手机可能拿不到DeviceId，会返回null或者000000。
         * 这个方法是只对有电话功能的设备有效的，在pad上不起作用。 可以看下方法注释
         */
        public String imei;
        /**
         * MAC地址
         */
        public String mac;
        /**
         * AndroidId
         * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来。不需要权限，平板设备通用。获取成功率也较高，缺点是设备恢复出厂设置会重置。另外就是某些厂商的低版本系统会有bug，返回的都是相同的AndroidId。
         */
        public String androidId;
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
                    "imei='" + imei + '\'' +
                    ", mac='" + mac + '\'' +
                    ", androidId='" + androidId + '\'' +
                    ", releaseVersion='" + releaseVersion + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", brand='" + brand + '\'' +
                    ", model='" + model + '\'' +
                    ", sdkVersion=" + sdkVersion +
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
        mPhoneStatus.androidId = Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        mPhoneStatus.mac = getMacAddress();

        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mPhoneStatus.imei = tm.getDeviceId();
        }
        if (tm != null &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mPhoneStatus.phoneNumber = tm.getLine1Number();
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
                return pm.isScreenOn();
            }
        }
        return false;
    }

    public String getUuid() {
        String uuid = SPUtils.getInstance(mContext).get(KEY_UUID, "");
        Logger.d("从sp中获取uuid：" + uuid);
        if (uuid.isEmpty()) {
            uuid = readUuidFromFile(FILE_DCIM);
            Logger.d("从FILE_DCIM中获取uuid：" + uuid);
            if (uuid.isEmpty()) {
                uuid = readUuidFromFile(FILE_DOWNLOADS);
                Logger.d("从FILE_DOWNLOADS中获取uuid：" + uuid);
            }
        }
        if (uuid.isEmpty()) {
            uuid = createUuid();
            Logger.d("新创建了一个uuid：" + uuid);
        }
        return uuid;
    }

    /**
     * 创建设备唯一标识
     *
     * @return
     */
    private String createUuid() {
        String uuid;
        if (mPhoneStatus.imei != null && !mPhoneStatus.imei.isEmpty()) {
            uuid = mPhoneStatus.imei;
            Logger.d("imei为uuid");
        } else if (mPhoneStatus.mac != null && !mPhoneStatus.mac.isEmpty()) {
            uuid = mPhoneStatus.mac;
            Logger.d("mac为uuid");
        } else if (mPhoneStatus.androidId != null && !mPhoneStatus.androidId.isEmpty()) {
            uuid = mPhoneStatus.androidId;
            Logger.d("androidId为uuid");
        } else {
            uuid = UUID.randomUUID().toString();
            Logger.d("randomUUID为uuid");
        }
        if (uuid != null && !uuid.isEmpty()) {
            saveUuidToFile(FILE_DCIM, uuid);
            saveUuidToFile(FILE_DOWNLOADS, uuid);
            SPUtils.getInstance(mContext).put(KEY_UUID, uuid);
        }
        return uuid;
    }

    private String readUuidFromFile(String fileName) {
        BufferedReader reader = null;
        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUuidToFile(String fileName, String uuid) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(fileName));
            writer.write(uuid);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getMacAddress() {
        String mac = null;
        FileReader fstream = null;
        try {
            fstream = new FileReader("/sys/class/net/wlan0/address");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                fstream = new FileReader("/sys/class/net/eth0/address");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        if (fstream != null) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(fstream, 1024);
                mac = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fstream != null) {
                    try {
                        fstream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mac;
    }
}
