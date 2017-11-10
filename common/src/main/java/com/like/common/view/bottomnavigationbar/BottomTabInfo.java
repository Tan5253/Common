package com.like.common.view.bottomnavigationbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * 每个tab项的基类
 */
public abstract class BottomTabInfo {
    protected Context mContext;
    /**
     * 所在位置索引，从0开始
     */
    private int mIndex;
    /**
     * 是否已经选中
     */
    private boolean isSelected = false;
    /**
     * 是否第一次加载图片
     */
    private boolean isFirstLoadImage = true;
    /**
     * 未选中时的图标
     */
    private Drawable mImageNormal;
    /**
     * 选中时的图标
     */
    private Drawable mImagePress;
    /**
     * 显示图标的ImageView
     */
    private ImageView mImageView;

    private View mTabView;

    /**
     * 每个tab的选中监听
     */
    private OnTabSelectedListener mTabSelectedListener;

    BottomTabInfo(Context context, int index, OnTabSelectedListener tabSelectedListener) {
        if (context == null || index < 0 || tabSelectedListener == null) {
            throw new IllegalArgumentException("BottomTabInfo的构造函数中存在无效参数");
        }
        this.mContext = context;
        this.mIndex = index;
        this.mTabSelectedListener = tabSelectedListener;
    }

    /**
     * 初始化
     */
    void init(View tabView, ImageView icon) {
        if (tabView == null || icon == null) {
            throw new IllegalArgumentException("BottomTabInfo的init函数中存在无效参数");
        }
        this.mTabView = tabView;
        this.mImageView = icon;
        tabView.setOnClickListener(v -> {
            select();
            mTabSelectedListener.onSelected(mIndex);
        });
    }

    /**
     * 选中按钮
     */
    synchronized void select() {
        if (mImagePress == null) {
            throw new RuntimeException("BottomTabInfo 您还没有设置按下状态下的图标");
        }
        if (!isFirstLoadImage && isSelected) {
            return;
        }
        mImageView.setImageDrawable(mImagePress);

        isSelected = true;
        isFirstLoadImage = false;
    }

    /**
     * 取消选中
     */
    synchronized void deselect() {
        if (mImageNormal == null) {
            throw new RuntimeException("BottomTabInfo 您还没有设置正常状态下的图标");
        }
        if (!isFirstLoadImage && !isSelected) {
            return;
        }
        mImageView.setImageDrawable(mImageNormal);

        isSelected = false;
        isFirstLoadImage = false;
    }

    /**
     * 是否选中
     */
    boolean isSelected() {
        return isSelected;
    }

    /**
     * 设置没有选中时的图标
     */
    void setImageNormal(Drawable imageNormal) {
        mImageNormal = imageNormal;
        if (this instanceof BottomTabInfoNew)// 必须重新设置，否则不会超出Bottom的高度
            mImageView.getLayoutParams().height = mImageNormal.getIntrinsicHeight();
    }

    /**
     * 设置选中时的图标
     */
    void setImagePress(Drawable imagePress) {
        mImagePress = imagePress;
        if (this instanceof BottomTabInfoNew)// 必须重新设置，否则不会超出Bottom的高度
            mImageView.getLayoutParams().height = mImagePress.getIntrinsicHeight();
    }

    View getView() {
        return mTabView;
    }
}
