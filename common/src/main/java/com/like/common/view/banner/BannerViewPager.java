package com.like.common.view.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以控制是否左右滑动的ViewPager{@link #setScrollable(boolean)}，默认不能滑动
 */
public class BannerViewPager extends ViewPager {
    private boolean isScrollable = false;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置缓存，这样左右拖动即可看见后面的Fragment。
        this.setOffscreenPageLimit(3);
    }

    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

}
