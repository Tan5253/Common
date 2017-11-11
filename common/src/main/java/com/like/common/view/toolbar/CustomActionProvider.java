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
import com.like.common.util.BadgeViewHelper;

/**
 * 替换Toolbar的menu为自定义视图
 */
public class CustomActionProvider extends ActionProvider {
    private ToolbarCustomViewBinding mBinding;
    private BadgeViewHelper badgeViewHelper;

    public CustomActionProvider(Context context) {
        super(context);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.toolbar_custom_view, null, false);
        badgeViewHelper = new BadgeViewHelper(context, mBinding.messageContainer);
    }

    @Override
    public View onCreateActionView() {
        return mBinding.getRoot();
    }

    void setOnClickListener(View.OnClickListener clickListener) {
        mBinding.getRoot().setOnClickListener(view -> {
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

    void setMessageCount(String messageCount) {
        badgeViewHelper.setMessageCount(messageCount);
    }
}
