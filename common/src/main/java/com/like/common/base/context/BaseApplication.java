package com.like.common.base.context;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.umeng.analytics.MobclickAgent;

/**
 * 初始化了友盟统计
 */
public class BaseApplication extends Application {
    public static boolean openUMeng = true;

    @Override
    public void onCreate() {
        super.onCreate();
        if (openUMeng) {
            MobclickAgent.setDebugMode(true);// 友盟统计：必须开启日志才能进行统计。
//        MobclickAgent.enableEncrypt(true);// 友盟统计：日志加密
            MobclickAgent.openActivityDurationTrack(false);// 友盟统计：禁止默认的页面统计方式，这样将不会再自动统计Activity，更改为在BaseActivity和BaseFragment添加代码来统计。
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 65535限制
        MultiDex.install(this);
    }

}
