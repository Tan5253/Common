package com.like.common.view.bottomNavigationBars;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.like.common.R;

/**
 * 原始的Tab
 */
public class BottomTabInfoOrigin extends BottomTabInfo {
    private TextView title;
    /**
     * 正常文本颜色
     */
    private int mTextColorNormal;
    /**
     * 按下文本颜色
     */
    private int mTextColorPress;

    public BottomTabInfoOrigin(Context context, int index, OnTabSelectedListener tabSelectedListener) {
        super(context, index, tabSelectedListener);
    }

    public void initView(String name) {
        View tabView = View.inflate(mContext, R.layout.view_activity_bottom_tab_origin, null);
        tabView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageView icon = (ImageView) tabView.findViewById(R.id.icon);
        title = (TextView) tabView.findViewById(R.id.title);
        View messageContainer = tabView.findViewById(R.id.messageContainer);

        title.setText(name);

        super.init(tabView, icon, messageContainer);
    }

    @Override
    public void setTextColorNormal(int textColorNormal) {
        mTextColorNormal = textColorNormal;
    }

    @Override
    public void setTextColorPress(int textColorPress) {
        mTextColorPress = textColorPress;
    }

    @Override
    public void setTextColorNormalByResId(int normalTextColorResId) {
        mTextColorNormal = mContext.getResources().getColor(normalTextColorResId);
    }

    @Override
    public void setTextColorPressByResId(int pressTextColorResId) {
        mTextColorPress = mContext.getResources().getColor(pressTextColorResId);
    }

    @Override
    protected void select() {
        super.select();
        title.setTextColor(mTextColorPress);
    }

    @Override
    protected synchronized void deselect() {
        super.deselect();
        title.setTextColor(mTextColorNormal);
    }

}
