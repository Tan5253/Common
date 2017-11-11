package com.like.common.util;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.View;

import com.like.common.view.BadgeView;

/**
 * 未读消息数量显示的徽章视图，注意，target需要有ViewGroup的parent。
 */
public class BadgeViewHelper {
    private BadgeView badgeView;

    public BadgeViewHelper(Context context, View target) {
        badgeView = new BadgeView(context);
        badgeView.setTargetView(target);
    }

    public void setTextColor(@ColorInt int color) {
        badgeView.setTextColor(color);
    }

    public void setTextSize(float size) {
        badgeView.setTextSize(size);
    }

    public void setBackgroundColor(@ColorInt int color) {
        badgeView.setBackgroundColor(color);
    }

    public void setMessageCount(String messageCount) {
        badgeView.setBadgeCount(messageCount);
    }
}
