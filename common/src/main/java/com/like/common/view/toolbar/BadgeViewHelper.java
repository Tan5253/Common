package com.like.common.view.toolbar;

import android.content.Context;
import android.view.View;

import com.like.common.view.BadgeView;

public class BadgeViewHelper {
    private BadgeView badgeView;

    public BadgeViewHelper(Context context,View target) {
        badgeView = new BadgeView(context);
        badgeView.setTargetView(target);
    }

    public void setMessageCount(int messageCount) {
        badgeView.setBadgeCount(messageCount);
    }
}
