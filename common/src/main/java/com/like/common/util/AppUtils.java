package com.like.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Window;

import com.like.logger.Logger;

import java.io.File;
import java.util.List;

/**
 * app相关工具类
 * <p>
 * Manifest.permission.WRITE_SETTINGS
 */
public class AppUtils {
    /**
     * 获取渠道来源的key
     */
    public static final String KEY_DOWNLOAD_CHANNEL = "CHANNEL";
    /**
     * 平台类型
     */
    public static final int PLATFORM_TYPE = 0;
    public AppStatus mAppStatus;
    private Context mContext;

    private volatile static AppUtils sInstance = null;

    public static AppUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppUtils.class) {
                if (sInstance == null) {
                    sInstance = new AppUtils(context);
                }
            }
        }
        return sInstance;
    }

    private AppUtils(Context context) {
        mContext = context.getApplicationContext();
        initAppStatus();
    }

    /**
     * app相关的状态信息
     */
    public static class AppStatus {
        /**
         * 版本号码
         */
        public int versionCode;
        /**
         * 版本名称
         */
        public String versionName;
        /**
         * 渠道号码
         */
        public String downSource;
        /**
         * 包名
         */
        public String packageName;
        /**
         * 平台类型
         */
        public int platformType;
        /**
         * 签名信息
         */
        public String sign;

        @Override
        public String toString() {
            return "AppStatus{" +
                    "versionCode=" + versionCode +
                    ", versionName='" + versionName + '\'' +
                    ", downSource='" + downSource + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", platformType=" + platformType +
                    ", sign='" + sign + '\'' +
                    '}';
        }
    }

    /**
     * 获取应用相关的状态信息
     */
    private void initAppStatus() {
        mAppStatus = new AppStatus();
        mAppStatus.platformType = PLATFORM_TYPE;
        PackageManager pm = mContext.getPackageManager();
        String packageName = mContext.getPackageName();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            mAppStatus.packageName = pi.packageName;
            mAppStatus.versionCode = pi.versionCode;
            mAppStatus.versionName = pi.versionName;
        } catch (NameNotFoundException e) {
            Logger.e("获得应用packageName、versionCode、versionName失败 " + e.getMessage());
        }
        try {
            mAppStatus.sign = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0].toCharsString();
        } catch (NameNotFoundException e) {
            Logger.e("获得应用sign失败 " + e.getMessage());
        }
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            mAppStatus.downSource = appInfo.metaData == null ? "" : appInfo.metaData.getString(KEY_DOWNLOAD_CHANNEL);
        } catch (NameNotFoundException e) {
            Logger.e("获得应用downSource信息失败 " + e.getMessage());
        }
        Logger.i(mAppStatus);
    }

    /**
     * APP是否正在运行，并且baseActivity为MainActivity
     *
     * @param context
     * @return
     */
    public static boolean isAppRunning(Context context) {
        boolean isRunning = false;
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(sInstance.mAppStatus.packageName) &&
                    info.baseActivity.getPackageName().equals(sInstance.mAppStatus.packageName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static String getBaseActivityName(Context context) {
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(sInstance.mAppStatus.packageName)) {
                return info.baseActivity.getClassName();
            }
        }
        return "";
    }

    /**
     * 判断服务是否启动, 注意只要名称相同, 会检测任何服务.
     *
     * @param context
     * @param serviceClass 需要判断的服务类
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServices = am.getRunningServices(Integer.MAX_VALUE);// 参数表示需要获取的正在运行的服务数量，这里我们取最大值
        if (runningServices != null && !runningServices.isEmpty()) {
            for (RunningServiceInfo r : runningServices) {
                // 添加Uid验证, 防止服务重名, 当前服务无法启动
                if (getUid(context) == r.uid) {
                    if (serviceClass.getName().equals(r.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取应用的Uid, 用于验证服务是否启动
     *
     * @param context 上下文
     * @return uid
     */
    private static int getUid(Context context) {
        if (context == null) {
            return -1;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
            if (infos != null && !infos.isEmpty()) {
                for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
                    if (processInfo.pid == pid) {
                        return processInfo.uid;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 判断某个activity是否处于前台
     * <uses-permission android:name = "android.permission.GET_TASKS"/>
     *
     * @param context
     * @param cls     需要判断的activity的全类名
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean isTopActivity(Context context, String cls) {
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (!applicationContext.getPackageName().equals(cn.getPackageName()) || !cls.equals(cn.getClassName())) {
            return false;
        }
        return true;
    }

    /**
     * 安装文件，如果直接传入Uri进行setDataAndType，在6.0及其以上会报错
     *
     * @param file
     */
    public static void installFile(Context context, File file) {
        Context applicationContext = context.getApplicationContext();
        try {
            Intent installIntent = new Intent();
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setAction(Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            applicationContext.startActivity(installIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取通知栏和标题栏的总高度
     *
     * @param activity
     * @return
     */
    public static int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 跳转到应用设置页面
     *
     * @param context
     */
    public static void gotoAppDetailSettingActivity(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", sInstance.mAppStatus.packageName, null));
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取进程名称
     *
     * @param context 上下文
     * @return 进程名称
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     * 检测应用是否运行
     *
     * @param packageName 包名
     * @param context     上下文
     * @return 是否存在
     */
    public static boolean isAppAlive(String packageName, Context context) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
            if (procInfos != null && !procInfos.isEmpty()) {
                for (int i = 0; i < procInfos.size(); i++) {
                    if (procInfos.get(i).processName.equals(packageName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
