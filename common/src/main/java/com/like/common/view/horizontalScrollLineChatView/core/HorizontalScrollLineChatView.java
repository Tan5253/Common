package com.like.common.view.horizontalScrollLineChatView.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * 可水平滑动的图表视图，只需要在xml文件中写上此视图即可用，必须调用init()方法
 */
public class HorizontalScrollLineChatView extends HorizontalScrollView {
    private LineChatView lineChatView;

    public HorizontalScrollLineChatView(Context context) {
        super(context);
        initView(context);
    }

    public HorizontalScrollLineChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
//        Log.i("TAG", "HorizontalScrollLineChatView initView()");
        lineChatView = new LineChatView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lineChatView.setLayoutParams(layoutParams);
        addView(lineChatView);
    }

    /**
     * 必须初始化才能使用
     *
     * @param lineChatHelper
     * @param showNumber     一屏显示的数据数量
     * @param showWidth      图表视图显示在屏幕上面的宽度
     * @param showHeight     图表视图显示在屏幕上面的高度
     */
    public void init(LineChatHelper lineChatHelper, int showNumber, float showWidth, float showHeight) {
        lineChatView.init(lineChatHelper, showNumber, showWidth, showHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;// 避免点击图表时不翻转
    }
}
