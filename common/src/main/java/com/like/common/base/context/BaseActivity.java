package com.like.common.base.context;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Window;

import com.like.common.base.entity.Host;
import com.like.common.base.viewmodel.BaseViewModel;
import com.like.rxbus.RxBus;
import com.like.rxbus.annotations.RxBusSubscribe;
import com.like.toast.ToastUtilsKt;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 初始化了友盟统计、BaseViewModel的注册和解注册、RxBus注册和取消注册、关闭对应的Activity。
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    public static final String TAG_FINISH_ACTIVITY = "TAG_FINISH_ACTIVITY";// 关掉所有activity
    public static final String TAG_FINISH_ACTIVITY_EXCLUDE = "TAG_FINISH_ACTIVITY_EXCLUDE";// 关掉除了指定的activity以外的所有activity
    public static final String TAG_FINISH_SOME_ACTIVITY = "TAG_FINISH_SOME_ACTIVITY";// 关掉指定的activity
    protected BaseViewModel mViewModel;
    protected Host host;

    /**
     * 双击返回键退出程序的时间间隔
     */
    private static final int INTERVAL_DOUBLE_PRESS_BACK_TO_EXIT = 2000;
    /**
     * 是否第一次按下返回键，用于双击退出
     */
    private boolean isFirstPressBack;
    private Handler doublePressBackToExitHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            isFirstPressBack = false;
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        host = new Host(this);
        RxBus.register(this);
        mViewModel = getViewModel();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        if (BaseApplication.openUMeng) {
            if (getSupportFragmentManager() == null || getSupportFragmentManager().getFragments() == null || getSupportFragmentManager().getFragments().size() == 0) {
                // 有Fragment时，此处就不要了，就要在Fragment中添加这句代码。
                MobclickAgent.onPageStart(this.getClass().getName()); // 友盟统计：统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。参数为页面名称，可自定义)
            }
            MobclickAgent.onResume(this);// 友盟统计：
        }
        host.setForeground(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onPause() {
        super.onPause();
        if (BaseApplication.openUMeng) {
            if (getSupportFragmentManager() == null || getSupportFragmentManager().getFragments() == null || getSupportFragmentManager().getFragments().size() == 0) {
                // 有Fragment时，此处就不要了，就要在Fragment中添加这句代码。
                MobclickAgent.onPageEnd(this.getClass().getName()); // 友盟统计：（仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。参数为页面名称，可自定义
            }
            MobclickAgent.onPause(this);// 友盟统计：
        }
        host.setForeground(false);
    }

    /**
     * 如果需要双击退出界面，请重写此方法
     *
     * @return
     */
    protected boolean isSupportDoublePressBackToExit() {
        return false;
    }

    @Override
    public void onBackPressed() {
        // 双击返回键退出程序
        if (isSupportDoublePressBackToExit() && !isFirstPressBack) {
            ToastUtilsKt.shortToastCenter(this, "再按一次返回键退出程序");
            isFirstPressBack = true;
            doublePressBackToExitHandler.sendEmptyMessageDelayed(0, INTERVAL_DOUBLE_PRESS_BACK_TO_EXIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.unregister(this);
        if (mViewModel != null)
            mViewModel.unregister();
        if (doublePressBackToExitHandler != null) {
            doublePressBackToExitHandler.removeCallbacksAndMessages(null);
        }
    }

    protected abstract BaseViewModel getViewModel();

    @RxBusSubscribe(TAG_FINISH_ACTIVITY)
    public void finishActivity() {
        // 关闭对应的Activity
        this.finish();
    }

    @RxBusSubscribe(TAG_FINISH_ACTIVITY_EXCLUDE)
    public void finishActivityExclude(List<Class> classes) {
        if (!classes.contains(this.getClass())) {
            // 关闭对应的Activity，除了自己
            this.finish();
        }
    }

    @RxBusSubscribe(TAG_FINISH_SOME_ACTIVITY)
    public void finishSomeActivity(List<Class> classes) {
        if (classes.contains(this.getClass())) {
            // 关闭指定的Activity
            this.finish();
        }
    }

}
