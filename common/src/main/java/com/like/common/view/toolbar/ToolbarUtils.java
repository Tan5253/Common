package com.like.common.view.toolbar;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.like.common.R;
import com.like.common.databinding.ToolbarBinding;
import com.like.common.util.BadgeViewHelper;

/**
 * Toolbar相关工具类
 */
public class ToolbarUtils {
    private Activity mActivity;
    private ToolbarBinding mBinding;
    private BadgeViewHelper navigationBadgeViewHelper;

    public ToolbarUtils(Activity activity, ViewGroup toolbarContainer) {
        if (activity != null && toolbarContainer != null) {
            mActivity = activity;
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.toolbar, toolbarContainer, true);
            mBinding.toolbar.setTitle("");// 屏蔽掉原来的标题
        }
    }

    public ToolbarUtils setToolbarHeight(int height) {
        mBinding.toolbar.getLayoutParams().height = height;
        mBinding.toolbar.setMinimumHeight(height);// 设置最小高度才能使自定义的menu视图居中
        return this;
    }

    public int getToolbarHeight() {
        return mBinding.getRoot().getHeight();
    }

    /**
     * 显示标题栏底部的分割线，默认是显示的
     */
    public ToolbarUtils showDivider() {
        mBinding.divider.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 隐藏标题栏底部的分割线
     */
    public ToolbarUtils hideDivider() {
        mBinding.divider.setVisibility(View.GONE);
        return this;
    }

    public ToolbarUtils setDividerHeight(int height) {
        mBinding.divider.getLayoutParams().height = height;
        return this;
    }


    /**
     * 设置标题栏底部的分割线的颜色
     */
    public ToolbarUtils setDividerColor(@ColorInt int corlor) {
        mBinding.divider.setBackgroundColor(corlor);
        return this;
    }

    /**
     * 设置标题栏背景颜色
     *
     * @return
     */
    public ToolbarUtils setBackgroundByColor(@ColorInt int color) {
        mBinding.toolbar.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置标题栏背景颜色
     *
     * @return
     */
    public ToolbarUtils setBackgroundByColorResId(@ColorRes int colorResId) {
        mBinding.toolbar.setBackgroundColor(mActivity.getResources().getColor(colorResId));
        return this;
    }

    /**
     * 显示返回按钮
     *
     * @return
     */
    public ToolbarUtils showBackButton() {
        mBinding.toolbar.setNavigationOnClickListener(view -> mActivity.finish());
        return this;
    }

    /**
     * 显示返回按钮
     */
    public ToolbarUtils showBackButton(@DrawableRes int iconResId) {
        if (iconResId > 0)
            mBinding.toolbar.setNavigationIcon(iconResId);
        showBackButton();
        return this;
    }

    /**
     * 设置自定义视图的导航按钮
     */
    public ToolbarUtils showCustomNavigationView(@DrawableRes int iconResId, String name, View.OnClickListener listener) {
        hideNavigationBotton();
        navigationBadgeViewHelper = new BadgeViewHelper(mActivity, mBinding.toolbarNavigationCustomView.messageContainer);
        if (!TextUtils.isEmpty(name)) {
            mBinding.toolbarNavigationCustomView.tvTitle.setVisibility(View.VISIBLE);
            mBinding.toolbarNavigationCustomView.tvTitle.setText(name);
        }
        if (iconResId > 0) {
            mBinding.toolbarNavigationCustomView.iv.setVisibility(View.VISIBLE);
            mBinding.toolbarNavigationCustomView.iv.setImageResource(iconResId);
        }
        if (listener != null)
            mBinding.toolbarNavigationCustomView.getRoot().setOnClickListener(listener);
        return this;
    }

    /**
     * 设置自定义视图的导航按钮文本颜色
     */
    public ToolbarUtils setCustomNavigationViewTextColor(int color) {
        mBinding.toolbarNavigationCustomView.tvTitle.setTextColor(color);
        return this;
    }

    /**
     * 设置自定义视图的导航按钮文本大小
     */
    public ToolbarUtils setCustomNavigationViewTextSize(float size) {
        mBinding.toolbarNavigationCustomView.tvTitle.setTextSize(size);
        return this;
    }

    /**
     * 设置自定义视图的左边距
     */
    public ToolbarUtils setNavigationViewLeftMargin(int leftMargin) {
        mBinding.toolbar.setContentInsetsAbsolute(leftMargin, 0);
        return this;
    }

    /**
     * 设置自定义视图的导航按钮右上角显示的消息数
     */
    public ToolbarUtils setCustomNavigationViewMessageCount(String messageCount) {
        navigationBadgeViewHelper.setMessageCount(messageCount);
        return this;
    }

    /**
     * 设置自定义视图的导航按钮右上角显示的消息数的文本颜色
     */
    public ToolbarUtils setCustomNavigationViewMessageTextColor(@ColorInt int color) {
        navigationBadgeViewHelper.setTextColor(color);
        return this;
    }

    public ToolbarUtils setCustomNavigationViewMessageTextSize(float size) {
        navigationBadgeViewHelper.setTextSize(size);
        return this;

    }

    public ToolbarUtils setCustomNavigationViewMessageBackgroundColor(@ColorInt int color) {
        navigationBadgeViewHelper.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置导航按钮
     *
     * @param navigationIconResId 导航按钮图片资源id
     * @param listener            导航按钮单击事件
     * @return
     */
    public ToolbarUtils showNavigationButton(@DrawableRes int navigationIconResId, View.OnClickListener listener) {
        if (navigationIconResId > 0)
            mBinding.toolbar.setNavigationIcon(navigationIconResId);
        if (listener != null)
            mBinding.toolbar.setNavigationOnClickListener(listener);
        return this;
    }


    /**
     * 屏蔽掉navigation按钮
     *
     * @return
     */
    public ToolbarUtils hideNavigationBotton() {
        mBinding.toolbar.setNavigationIcon(null);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @param colorResId 文本颜色
     * @return
     */
    public ToolbarUtils showTitle(String title, @ColorRes int colorResId) {
        mBinding.tvTitle.setTextColor(mActivity.getResources().getColor(colorResId));
        mBinding.tvTitle.setText(title);
        return this;
    }

    /**
     * 设置Toolbar右侧(标题右侧)的几个菜单按钮
     *
     * @param menuResId
     * @param listener
     * @return
     */
    public ToolbarUtils setRightMenu(@MenuRes int menuResId, Toolbar.OnMenuItemClickListener listener) {
        if (menuResId > 0)
            mBinding.toolbar.inflateMenu(menuResId);
        if (listener != null)
            mBinding.toolbar.setOnMenuItemClickListener(listener);
        return this;
    }

    /**
     * 替换menu为自定义的视图，需要先调用setRightMenu()方法
     *
     * @param menuId
     * @param iconResId     <=0即不显示图片
     * @param name          为empty时即不显示名称
     * @param clickListener
     * @return
     */
    public ToolbarUtils replaceMenuWithCustomView(int menuId, @DrawableRes int iconResId, String name, View.OnClickListener clickListener) {
        return replaceMenuWithCustomView(menuId, iconResId, name, true, clickListener);
    }

    /**
     * 替换menu为自定义的视图。并设置是否隐藏
     *
     * @param menuId
     * @param iconResId     <=0即不显示图片
     * @param name          为empty时即不显示名称
     * @param isShow        是否立即显示
     * @param clickListener
     * @return
     */
    public ToolbarUtils replaceMenuWithCustomView(int menuId, @DrawableRes int iconResId, String name, boolean isShow, View.OnClickListener clickListener) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.reset();
            customActionProvider.setOnClickListener(clickListener);
            if (!TextUtils.isEmpty(name))
                customActionProvider.setName(name);
            if (iconResId > 0)
                customActionProvider.setIcon(iconResId);
            if (isShow) {
                customActionProvider.show();
            } else {
                customActionProvider.hide();
            }
        }
        return this;
    }

    /**
     * 设置右边指定菜单按钮的消息数量
     *
     * @param menuId
     * @param messageCount
     * @return
     */
    public ToolbarUtils setRightMenuMessageCount(int menuId, String messageCount) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.setMessageCount(messageCount);
        }
        return this;
    }

    /**
     * 设置右边指定菜单按钮的文本颜色
     *
     * @param menuId
     * @param color
     * @return
     */
    public ToolbarUtils setRightMenuTextColor(int menuId, @ColorInt int color) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置右边指定菜单按钮的文本大小
     *
     * @param menuId
     * @param size   单位sp
     * @return
     */
    public ToolbarUtils setRightMenuTextSize(int menuId, float size) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.setTextSize(size);
        }
        return this;
    }

    /**
     * 设置右边指定菜单按钮的文本
     *
     * @param menuId
     * @param name
     * @return
     */
    public ToolbarUtils setRightMenuName(int menuId, String name) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            if (!TextUtils.isEmpty(name))
                customActionProvider.setName(name);
        }
        return this;
    }

    /**
     * 设置右边指定菜单按钮的左右margin
     */
    public ToolbarUtils setRightMenuMargin(int menuId, int leftAndRightMargin, int topAndBottomMargin) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.setMargin(leftAndRightMargin, topAndBottomMargin);
        }
        return this;
    }

    /**
     * 设置右边指定菜单按钮的消息视图距离图标的距离
     */
    public ToolbarUtils setRightMenuMessageMargin(int menuId, int left, int top, int right, int bottom) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.setMessageMargin(left, top, right, bottom);
        }
        return this;
    }

    /**
     * 获取右边指定菜单按钮的文本
     *
     * @param menuId
     * @return
     */
    public String getRightMenuName(int menuId) {
        String name = "";
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            name = customActionProvider.getName();
        }
        return name;
    }

    /**
     * 隐藏右边指定菜单
     *
     * @param menuId
     */
    public void hideRightMenu(int menuId) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.hide();
        }
    }

    /**
     * 显示右边指定菜单
     *
     * @param menuId
     */
    public void showRightMenu(int menuId) {
        CustomActionProvider customActionProvider = getCustomActionProvider(menuId);
        if (customActionProvider != null) {
            customActionProvider.show();
        }
    }

    /**
     * 获取控制message视图相关功能的Provider
     *
     * @param menuId
     * @return
     */
    private CustomActionProvider getCustomActionProvider(int menuId) {
        MenuItem item = mBinding.toolbar.getMenu().findItem(menuId);
        return (CustomActionProvider) MenuItemCompat.getActionProvider(item);
    }

}
