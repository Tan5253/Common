package com.like.common.view.bottomNavigationBars;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.like.common.R;
import com.like.common.view.BadgeView;

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
     * 未选中时的图标
     */
    private Drawable mImageNormal;
    /**
     * 选中时的图标
     */
    private Drawable mImagePress;
    /**
     * 是否已经选中
     */
    private boolean isSelected = false;
    /**
     * 是否第一次加载图片
     */
    private boolean isFirstLoadImage = true;
    /**
     * 显示图标的ImageView
     */
    private ImageView icon;

    private View tabView;

    /**
     * 每个tab的选中监听
     */
    private OnTabSelectedListener mTabSelectedListener;

    /**
     * 选中时tab的背景色
     */
    private int tabBgColorPress;
    /**
     * 选中时tab的背景图片
     */
    private Drawable tabBgImagePress;

    private int transparentColor;

    private BadgeView badgeView;

    BottomTabInfo(Context context, int index, OnTabSelectedListener tabSelectedListener) {
        if (context == null || index < 0 || tabSelectedListener == null) {
            throw new IllegalArgumentException("BottomTabInfo的构造函数中存在无效参数");
        }
        this.mContext = context;
        this.mIndex = index;
        this.mTabSelectedListener = tabSelectedListener;
        transparentColor = mContext.getResources().getColor(R.color.common_transparent);
    }

    /**
     * @param tabView
     * @param icon
     * @param messageContainer 消息边界限制容器
     */
    void init(View tabView, ImageView icon, View messageContainer) {
        if (tabView == null || icon == null) {
            throw new IllegalArgumentException("BottomTabInfo的init函数中存在无效参数");
        }
        this.tabView = tabView;
        this.icon = icon;
        tabView.setOnClickListener(v -> {
            select();
            mTabSelectedListener.onSelected(mIndex);
        });
        if (messageContainer != null && badgeView == null) {
            badgeView = new BadgeView(mContext);
            badgeView.setTargetView(messageContainer);
        }
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
        icon.setImageDrawable(mImagePress);
        tabView.setBackgroundColor(tabBgColorPress);
        if (tabBgImagePress != null) {
            tabView.setBackgroundDrawable(tabBgImagePress);
        }

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
        icon.setImageDrawable(mImageNormal);
        tabView.setBackgroundColor(transparentColor);
        tabView.setBackgroundDrawable(null);

        isSelected = false;
        isFirstLoadImage = false;
    }

    /**
     * 是否选中
     *
     * @return
     */
    boolean isSelected() {
        return isSelected;
    }

    /**
     * 设置没有选中时的图标
     *
     * @param imageNormal
     */
    void setImageNormal(Drawable imageNormal) {
        mImageNormal = imageNormal;
    }

    /**
     * 设置没有选中时的图标
     *
     * @param imageNormalResId
     */
    void setImageNormal(int imageNormalResId) {
        mImageNormal = mContext.getResources().getDrawable(imageNormalResId);
    }

    /**
     * 设置选中时的图标
     *
     * @param imagePress
     */
    void setImagePress(Drawable imagePress) {
        mImagePress = imagePress;
    }

    /**
     * 设置选中时的图标
     *
     * @param imagePressResId
     */
    void setImagePress(int imagePressResId) {
        mImagePress = mContext.getResources().getDrawable(imagePressResId);
    }

    /**
     * 设置消息数量，设置为0或者null时，隐藏
     *
     * @param count
     */
    void setMessageCount(int count) {
        String countStr = String.valueOf(count);
        if (count <= 0) {
            countStr = "0";
        } else if (count > 99) {
            countStr = "99+";
        }
        badgeView.setBadgeCount(countStr);

    }

    /**
     * 设置没有选中时的文字颜色，用于原始导航栏
     *
     * @param textColorNormal
     */
    void setTextColorNormal(int textColorNormal) {
    }

    /**
     * 设置没有选中时的文字颜色，用于原始导航栏
     *
     * @param normalTextColorResId
     */
    void setTextColorNormalByResId(int normalTextColorResId) {
    }

    /**
     * 设置选中时的文字颜色，用于原始导航栏
     *
     * @param textColorPress
     */
    void setTextColorPress(int textColorPress) {
    }

    /**
     * 设置选中时的文字颜色，用于原始导航栏
     *
     * @param pressTextColorResId
     */
    void setTextColorPressByResId(int pressTextColorResId) {
    }

    /**
     * 设置选中时的背景色
     *
     * @param tabBgColorPress
     */
    void setTabBgColorPress(int tabBgColorPress) {
        this.tabBgColorPress = tabBgColorPress;
    }

    /**
     * 设置选中时的背景图片
     *
     * @param tabBgImagePress
     */
    void setTabBgImagePress(Drawable tabBgImagePress) {
        this.tabBgImagePress = tabBgImagePress;
    }

    View getView() {
        return tabView;
    }
}
