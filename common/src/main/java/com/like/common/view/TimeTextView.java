package com.like.common.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * * 获取验证码倒计时按钮。 </br>
 * 由于timer每次cancle()之后不能重新schedule方法,所以计时关闭后.每次开始计时的时候重新设置timer。
 * 注意把该类的onCreate()onDestroy()和activity的onCreate()onDestroy()同步处理
 */
public class TimeTextView extends android.support.v7.widget.AppCompatTextView {
    // 用于存放倒计时时间
    public static Map<String, Long> sTimeMap;
    /**
     * Map中保存倒计时长的key
     */
    private final String TIME_LENGTH = "time_length";
    /**
     * Map中保存当前时间毫秒数的key
     */
    private final String CURRENT_TIME = "current_time";
    /**
     * 初始的倒计时长(毫秒)。默认60秒
     */
    private long initTimeLength = 60 * 1000;
    /**
     * 随时间变化的倒计时长
     */
    private long timeLength;
    private String textAfterPressFront = "";
    private String textAfterPressBehind = " s";
    private String textBeforePress = "获取验证码";
    private Timer timer;
    private TimerTask timerTask;

    public TimeTextView(Context context) {
        this(context, null);

    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText(textBeforePress);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            timeLength -= 1000;
            if (timeLength < 0) {
                TimeTextView.this.setEnabled(true);
                TimeTextView.this.setText(textBeforePress);
                clearTimer();
            } else {
                if (TimeTextView.this.isEnabled()) {
                    TimeTextView.this.setEnabled(false);
                }
                TimeTextView.this.setText(textAfterPressFront + timeLength / 1000 + textAfterPressBehind);
            }
            return true;
        }
    });

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (sTimeMap == null)
            sTimeMap = new HashMap<String, Long>();
        sTimeMap.put(TIME_LENGTH, timeLength);
        sTimeMap.put(CURRENT_TIME, System.currentTimeMillis());
        clearTimer();
    }

    /**
     * 和activity的onCreate()方法同步，默认倒计时60秒
     */
    public void onCreate() {
        if (sTimeMap == null || sTimeMap.size() <= 0) {
            return;
        }
        long time = System.currentTimeMillis() - sTimeMap.get(CURRENT_TIME) - sTimeMap.get(TIME_LENGTH);
        sTimeMap.clear();
        if (time < 0) {// 时间还没有走完，则继续走
            initTimer();
            this.timeLength = Math.abs(time);
            this.setEnabled(false);
            timer.schedule(timerTask, 0, 1000);
        }
    }

    /**
     * 和activity的onCreate()方法同步
     *
     * @param length 时间 默认毫秒
     */
    public void onCreate(long length) {
        this.initTimeLength = length;
        if (sTimeMap == null || sTimeMap.size() <= 0) {
            return;
        }
        long time = System.currentTimeMillis() - sTimeMap.get(CURRENT_TIME) - sTimeMap.get(TIME_LENGTH);
        sTimeMap.clear();
        if (time < 0) {// 时间还没有走完，则继续走
            initTimer();
            this.timeLength = Math.abs(time);
            this.setEnabled(false);
            timer.schedule(timerTask, 0, 1000);
        }
    }

    private void initTimer() {
        timeLength = initTimeLength;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 开始倒计时
     */
    public void execute() {
        initTimer();
        this.setEnabled(false);
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 设置点击之后计时时时间前面部分显示的文本
     *
     * @param text
     * @return
     */
    public TimeTextView setTextAfterPressFront(String text) {
        this.textAfterPressFront = text;
        return this;
    }

    /**
     * 设置点击之后计时时时间后面部分显示的文本
     *
     * @param text
     * @return
     */
    public TimeTextView setTextAfterPressBehind(String text) {
        this.textAfterPressBehind = text;
        return this;
    }

    /**
     * 设置点击之前的文本
     *
     * @param text
     * @return
     */
    public TimeTextView setTextBeforePress(String text) {
        this.textBeforePress = text;
        this.setText(textBeforePress);
        return this;
    }

}