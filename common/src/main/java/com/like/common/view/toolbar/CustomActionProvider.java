package com.like.common.view.toolbar;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

import com.like.common.R;
import com.like.common.databinding.ToolbarCustomViewBinding;
import com.like.common.view.BadgeView;

/**
 * 替换Toolbar的menu为message视图
 */
public class CustomActionProvider extends ActionProvider {
    private Context mContext;
    private ToolbarCustomViewBinding mBinding;
    private BadgeView badgeView;

    public CustomActionProvider(Context context) {
        super(context);
        mContext = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.toolbar_custom_view, null, false);
        badgeView = new BadgeView(context);
        badgeView.setTargetView(mBinding.messageContainer);
    }

    @Override
    public View onCreateActionView() {
        return mBinding.getRoot();
    }

    void setOnClickListener(View.OnClickListener clickListener) {
        mBinding.getRoot().setOnClickListener(view -> {
            setMessageCount(0);
            if (clickListener != null) {
                clickListener.onClick(view);
            }
        });
    }

    void hide() {
        mBinding.getRoot().setVisibility(View.GONE);
    }

    void show() {
        mBinding.getRoot().setVisibility(View.VISIBLE);
    }

    void setName(String name) {
        mBinding.tvTitle.setVisibility(View.VISIBLE);
        mBinding.tvTitle.setText(name);
    }

    void setTextColor(@ColorInt int color) {
        mBinding.tvTitle.setTextColor(color);
    }

    /**
     * @param size 单位sp
     */
    void setTextSize(float size) {
        mBinding.tvTitle.setTextSize(size);
    }

    String getName() {
        return mBinding.tvTitle.getText().toString();
    }

    void setIcon(@DrawableRes int iconResId) {
        mBinding.iv.setVisibility(View.VISIBLE);
        mBinding.iv.setImageResource(iconResId);
    }

    void setMessageCount(int messageCount) {
        badgeView.setBadgeCount(messageCount);
    }
}
