package com.like.common.view.bottomnavigationbar;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.like.common.R;
import com.like.common.databinding.ItemBottomNavigationBarBinding;
import com.like.common.view.BadgeView;

/**
 * 原始的Tab
 */
public class BottomTabInfoOrigin extends BottomTabInfo {
    private ItemBottomNavigationBarBinding mBinding;
    private String name;
    /**
     * 正常文本颜色
     */
    private @ColorInt
    int mTextColorNormal;
    /**
     * 按下文本颜色
     */
    private @ColorInt
    int mTextColorPress;

    private BadgeView badgeView;

    public BottomTabInfoOrigin(Context context, int index, OnTabSelectedListener tabSelectedListener) {
        super(context, index, tabSelectedListener);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_bottom_navigation_bar, null, false);
        mBinding.getRoot().setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

        if (badgeView == null) {
            badgeView = new BadgeView(mContext);
            badgeView.setTargetView(mBinding.messageContainer);
        }
        super.init(mBinding.getRoot(), mBinding.icon);
    }

    public void setName(String name) {
        this.name = name;
        if (!TextUtils.isEmpty(name)) {
            mBinding.title.setText(name);
        } else {
            mBinding.title.setVisibility(View.GONE);
        }
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

    public void setTextColorNormal(@ColorInt int textColorNormal) {
        mTextColorNormal = textColorNormal;
    }

    public void setTextColorPress(@ColorInt int textColorPress) {
        mTextColorPress = textColorPress;
    }

    @Override
    protected void select() {
        super.select();
        if (!TextUtils.isEmpty(name))
            mBinding.title.setTextColor(mTextColorPress);
    }

    @Override
    protected synchronized void deselect() {
        super.deselect();
        if (!TextUtils.isEmpty(name))
            mBinding.title.setTextColor(mTextColorNormal);
    }

}
