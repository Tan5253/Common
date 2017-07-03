package com.like.common.util;

import android.view.Gravity;
import android.view.View;

import com.like.toast.ToastUtilsKt;

/**
 * 点击事件相关的工具类
 */

public class ClickUtils {
    /**
     * 设置点击监听，连击一定次数后触发clickListener
     *
     * @param clickTimes    点击次数
     * @param view
     * @param clickListener
     */
    public static void addOnClickListener(int clickTimes, View view, View.OnClickListener clickListener) {
        addOnClickListener(500, clickTimes, view, clickListener);
    }

    /**
     * 设置点击监听，连击一定次数后触发clickListener
     *
     * @param interval      两次点击的时间间隔
     * @param clickTimes    点击次数
     * @param view
     * @param clickListener
     */
    public static void addOnClickListener(long interval, int clickTimes, View view, View.OnClickListener clickListener) {
        view.setOnClickListener(new View.OnClickListener() {
            long firstTime;
            int count;

            @Override
            public void onClick(View v) {
                long secondTime = System.currentTimeMillis();
                // 判断每次点击的事件间隔是否符合连击的有效范围
                // 不符合时，有可能是连击的开始，否则就仅仅是单击
                if (secondTime - firstTime <= interval) {
                    count++;
                } else {
                    count = 1;
                }
                // 延迟，用于判断用户的点击操作是否结束
                firstTime = secondTime;
                if (count <= clickTimes) {
                    ToastUtilsKt.toast(view.getContext(), "连续点击次数：" + count, 1000, Gravity.BOTTOM);
                }
                if (count == clickTimes) {
                    clickListener.onClick(view);
                }
            }
        });
    }

    /**
     * 防抖动按钮点击（500毫秒内只触发一次点击事件）
     *
     * @param view
     * @param clickListener
     */
    public static void addOnClickListener(View view, View.OnClickListener clickListener) {
        RxJavaUtils.addOnClickListener(500, view, clickListener);
    }

    /**
     * 防抖动按钮点击（指定时间间隔内只触发一次点击事件）
     *
     * @param interval      时间间隔(毫秒)
     * @param view
     * @param clickListener
     */
    public static void addOnClickListener(long interval, View view, View.OnClickListener clickListener) {
        RxJavaUtils.addOnClickListener(interval, view, clickListener);
    }
}
