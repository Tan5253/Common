package com.like.common.util;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.like.base.entity.Host;

import java.lang.reflect.Method;

/**
 * 操作DialogFragment的工具类
 */
public class DialogFragmentUtils {
    /**
     * 显示指定的DialogFragment，并返回此DialogFragment的实例对象
     *
     * @param host
     * @param dialogFragmentClass 对话框类
     * @param bundle              构造函数需要的参数
     * @return
     */
    public static DialogFragment showDialog(Host host, Class<?> dialogFragmentClass, Bundle bundle) {
        if (dialogFragmentClass == null || host == null) {
            return null;
        }
        DialogFragment dialogFragment = null;
        try {
            // 反射创建对应DialogFragment的实例
            Method method;
            if (bundle != null) {
                method = dialogFragmentClass.getMethod("newInstance", Bundle.class);
                dialogFragment = (DialogFragment) method.invoke(null, bundle);
            } else {
                method = dialogFragmentClass.getMethod("newInstance");
                dialogFragment = (DialogFragment) method.invoke(null);
            }
            // 先移除，再添加并显示
            FragmentManager fm = host.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment preFragment = fm.findFragmentByTag(dialogFragmentClass.getSimpleName());
            if (preFragment != null) {
                ft.remove(preFragment);
            }
            if (dialogFragment != null) {
                ft.add(dialogFragment, dialogFragmentClass.getSimpleName());
                ft.commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialogFragment;
    }

    /**
     * 隐藏指定的DialogFragment
     *
     * @param host
     * @param dialogFragmentClass 对话框类
     */
    public static void hideDialog(Host host, Class<?> dialogFragmentClass) {
        if (dialogFragmentClass == null || host == null) {
            return;
        }
        FragmentManager fm = host.getFragmentManager();
        Fragment dialog = fm.findFragmentByTag(dialogFragmentClass.getSimpleName());
        if (dialog != null && !dialog.isHidden()) {
            ((DialogFragment) dialog).dismissAllowingStateLoss();
        }
    }

}
