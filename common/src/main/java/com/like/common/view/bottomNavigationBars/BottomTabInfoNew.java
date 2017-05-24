package com.like.common.view.bottomNavigationBars;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.like.common.R;

/**
 * 活动时候的Tab
 */
public class BottomTabInfoNew extends BottomTabInfo {

    public BottomTabInfoNew(Context context, int index, OnTabSelectedListener tabSelectedListener) {
        super(context, index, tabSelectedListener);
    }

    public void initView() {
        View tabView = View.inflate(mContext, R.layout.view_activity_bottom_tab_new, null);
        tabView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageView icon = (ImageView) tabView.findViewById(R.id.icon);
        super.init(tabView, icon, icon);
    }

}
