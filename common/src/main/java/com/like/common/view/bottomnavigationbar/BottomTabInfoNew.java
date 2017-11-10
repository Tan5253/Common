package com.like.common.view.bottomnavigationbar;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 活动时候的Tab
 */
public class BottomTabInfoNew extends BottomTabInfo {

    public BottomTabInfoNew(Context context, int index, OnTabSelectedListener tabSelectedListener) {
        super(context, index, tabSelectedListener);
        ImageView iv = new ImageView(mContext);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        lp.gravity = Gravity.BOTTOM;
        iv.setLayoutParams(lp);
        super.init(iv, iv);
    }

}
