package com.like.common.view.bottomnavigationbar;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.like.common.R;
import com.like.common.databinding.ViewBottomNavigationBarBinding;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBarsHelper {
    private Activity mActivity;
    private ViewBottomNavigationBarBinding mBinding;
    private List<BottomTabInfo> mTabInfoList;
    private OnTabSelectedListener mTabSelectedForViewPager;
    private int mCurPosition;
    private Resources mResources;
    /**
     * 当某个tab被选中的监听，用于本类中处理反选问题
     */
    private OnTabClickListener mTabSelectedListener = new OnTabClickListener() {
        @Override
        public void onClick(int selectedIndex) {
            // 通知Activity改变ViewPager页面
            if (mTabSelectedForViewPager != null) {
                mTabSelectedForViewPager.onSelected(selectedIndex);
                mTabSelectedForViewPager.onClick(selectedIndex);
            }
            // 取消前一次选中的tab
            for (int i = 0; i < mTabInfoList.size(); i++) {
                BottomTabInfo tabInfo = mTabInfoList.get(i);
                if (i != selectedIndex && tabInfo.isSelected()) {
                    tabInfo.deselect();
                }
            }
        }
    };

    public BottomNavigationBarsHelper(Activity activity, ViewGroup navigationbarContainer) {
        if (activity != null && navigationbarContainer != null) {
            mActivity = activity;
            mResources = activity.getResources();
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.view_bottom_navigation_bar, navigationbarContainer, true);
        }
    }

    public ViewBottomNavigationBarBinding getBinding() {
        return mBinding;
    }

    public BottomNavigationBarsHelper initOriginView(@DrawableRes int[] normalImageResIds, @DrawableRes int[] pressImageResIds) {
        if (normalImageResIds == null || pressImageResIds == null) {
            throw new IllegalArgumentException("参数错误");
        }
        if (normalImageResIds.length < 2 || normalImageResIds.length > 5) {
            throw new IllegalArgumentException("tab的数量必须在2-5个");
        }
        if (normalImageResIds.length != pressImageResIds.length) {
            throw new IllegalArgumentException("参数错误");
        }

        mTabInfoList = new ArrayList<>();
        for (int i = 0; i < normalImageResIds.length; i++) {
            BottomTabInfoOrigin tabInfo = new BottomTabInfoOrigin(mActivity, i, mTabSelectedListener);
            // 设置图标
            tabInfo.setImageNormal(mResources.getDrawable(normalImageResIds[i]));
            tabInfo.setImagePress(mResources.getDrawable(pressImageResIds[i]));

            addTabInfo(tabInfo);
        }
        selectByPos(mCurPosition);
        return this;
    }

    public BottomNavigationBarsHelper initOriginView(String[] names, @DrawableRes int[] normalImageResIds, @DrawableRes int[] pressImageResIds, @ColorRes int normalTextColorResId, @ColorRes int pressTextColorResId) {
        if (names == null || normalImageResIds == null || pressImageResIds == null || normalTextColorResId < 0 || pressTextColorResId < 0) {
            throw new IllegalArgumentException("参数错误");
        }
        if (names.length < 2 || names.length > 5) {
            throw new IllegalArgumentException("tab的数量必须在2-5个");
        }
        if (names.length != normalImageResIds.length || names.length != pressImageResIds.length) {
            throw new IllegalArgumentException("参数错误");
        }

        mTabInfoList = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            BottomTabInfoOrigin tabInfo = new BottomTabInfoOrigin(mActivity, i, mTabSelectedListener);
            // 设置图标
            tabInfo.setImageNormal(mResources.getDrawable(normalImageResIds[i]));
            tabInfo.setImagePress(mResources.getDrawable(pressImageResIds[i]));
            // 设置文字
            tabInfo.setTextColorNormal(mResources.getColor(normalTextColorResId));
            tabInfo.setTextColorPress(mResources.getColor(pressTextColorResId));

            tabInfo.setName(names[i]);

            addTabInfo(tabInfo);
        }
        selectByPos(mCurPosition);
        return this;
    }

    /**
     * 替换某个tab为新布局，新布局可以超出Bottom的高度范围，用于做活动。
     *
     * @param index
     * @param imageResId 正常和按下状态都是它
     */
    public BottomNavigationBarsHelper setNew(int index, @DrawableRes int imageResId) {
        setNew(index, imageResId, imageResId);
        return this;
    }

    /**
     * 替换某个tab为新布局，新布局可以超出Bottom的高度范围，用于做活动。
     *
     * @param index
     * @param normalImageResId
     * @param pressImageResId
     */
    public BottomNavigationBarsHelper setNew(int index, @DrawableRes int normalImageResId, @DrawableRes int pressImageResId) {
        setNew(index, mResources.getDrawable(normalImageResId), mResources.getDrawable(pressImageResId));
        return this;
    }

    /**
     * 替换某个tab为新布局，新布局可以超出Bottom的高度范围，用于做活动。
     *
     * @param index
     * @param image 正常和按下状态都是它
     */
    public BottomNavigationBarsHelper setNew(int index, Drawable image) {
        setNew(index, image, image);
        return this;
    }

    /**
     * 替换某个tab为新布局，新布局可以超出Bottom的高度范围，用于做活动。
     *
     * @param index
     * @param normalImage
     * @param pressImage
     */
    public BottomNavigationBarsHelper setNew(int index, Drawable normalImage, Drawable pressImage) {
        if (index > mTabInfoList.size() - 1) {
            throw new IllegalArgumentException("index参数错误");
        }
        BottomTabInfoNew tabInfo = new BottomTabInfoNew(mActivity, index, mTabSelectedListener);
        // 设置图标
        tabInfo.setImageNormal(normalImage);
        tabInfo.setImagePress(pressImage);

        replaceTabInfo(index, tabInfo);

        selectByPos(mCurPosition);
        return this;
    }

    private void addTabInfo(BottomTabInfo tabInfo) {
        mBinding.llTabContainer.addView(tabInfo.getView());
        mTabInfoList.add(tabInfo);
    }

    private void replaceTabInfo(int index, BottomTabInfo tabInfo) {
        mBinding.llTabContainer.removeViewAt(index);
        mBinding.llTabContainer.addView(tabInfo.getView(), index);

        mTabInfoList.remove(index);
        mTabInfoList.add(index, tabInfo);
    }

    /**
     * 设置底部导航栏的背景图片
     *
     * @param bottomBgImage
     */
    public BottomNavigationBarsHelper setBottomBgImage(Drawable bottomBgImage) {
        mBinding.llTabContainer.setBackground(bottomBgImage);
        return this;
    }

    /**
     * 设置底部导航栏的背景色，不包含divider
     *
     * @param bottomBgColor
     */
    public BottomNavigationBarsHelper setBottomBgColor(@ColorRes int bottomBgColor) {
        mBinding.llTabContainer.setBackgroundColor(mResources.getColor(bottomBgColor));
        return this;
    }

    /**
     * 设置底部导航栏的背景色，包含divider
     *
     * @param bottomBgColor
     */
    public BottomNavigationBarsHelper setBgColorRes(@ColorRes int bottomBgColor) {
        mBinding.rlNavigationBar.setBackgroundColor(mResources.getColor(bottomBgColor));
        return this;
    }

    /**
     * 设置底部导航栏的背景色，包含divider
     *
     * @param bottomBgColor
     */
    public BottomNavigationBarsHelper setBgColor(@ColorInt int bottomBgColor) {
        mBinding.rlNavigationBar.setBackgroundColor(bottomBgColor);
        return this;
    }

    /**
     * 显示隐藏divider
     */
    public BottomNavigationBarsHelper setDividerVisibility(int visibility) {
        mBinding.divider.setVisibility(visibility);
        return this;
    }

    /**
     * 显示隐藏所有按钮
     */
    public BottomNavigationBarsHelper setAllButtonVisibility(int visibility) {
        mBinding.llTabContainer.setVisibility(visibility);
        return this;
    }

    /**
     * tab视图的第一次显示，第一个tab选中，后面的都不选中。并且改变ViewPager的页面为tab0
     */
    public void selectByPos(int pos) {
        if (mTabInfoList == null || mTabInfoList.size() <= 0) {
            return;
        }
        for (int i = 0; i < mTabInfoList.size(); i++) {
            BottomTabInfo info = mTabInfoList.get(i);
            if (i == pos) {
                info.select();
                mCurPosition = pos;
            } else {
                info.deselect();
            }
        }
        // 通知Activity改变ViewPager页面
        if (mTabSelectedForViewPager != null) {
            mTabSelectedForViewPager.onSelected(pos);
        }
    }

    public BottomNavigationBarsHelper setTabSelectedListener(OnTabSelectedListener listener) {
        mTabSelectedForViewPager = listener;
        return this;
    }

    /**
     * 获取底部tab导航视图
     *
     * @return
     */
    public View getView() {
        return mBinding.getRoot();
    }

    /**
     * 设置消息提示数量
     *
     * @param index tab的索引，从0开始
     * @param count 消息数量 小于等于0时隐藏
     */
    public BottomNavigationBarsHelper setMessageCount(int index, int count) {
        if (index < mTabInfoList.size() && mTabInfoList.get(index) instanceof BottomTabInfoOrigin) {
            ((BottomTabInfoOrigin) mTabInfoList.get(index)).setMessageCount(count);
        }
        return this;
    }


    public BottomNavigationBarsHelper setMessageTextColor(int index, @ColorInt int color) {
        if (index < mTabInfoList.size() && mTabInfoList.get(index) instanceof BottomTabInfoOrigin) {
            ((BottomTabInfoOrigin) mTabInfoList.get(index)).setMessageTextColor(color);
        }
        return this;
    }

    public BottomNavigationBarsHelper setMessageTextSize(int index, int size) {
        if (index < mTabInfoList.size() && mTabInfoList.get(index) instanceof BottomTabInfoOrigin) {
            ((BottomTabInfoOrigin) mTabInfoList.get(index)).setMessageTextSize(size);
        }
        return this;
    }

    public BottomNavigationBarsHelper setMessageBackgroundColor(int index, @ColorInt int color) {
        if (index < mTabInfoList.size() && mTabInfoList.get(index) instanceof BottomTabInfoOrigin) {
            ((BottomTabInfoOrigin) mTabInfoList.get(index)).setMessageBackgroundColor(color);
        }
        return this;
    }

    public BottomNavigationBarsHelper setDividerColor(@ColorRes int color) {
        mBinding.divider.setBackgroundColor(mResources.getColor(color));
        return this;
    }

    public BottomNavigationBarsHelper setDividerHeight(int height) {
        mBinding.divider.getLayoutParams().height = height;
        return this;
    }

    public BottomNavigationBarsHelper setBottomHeight(int height) {
        mBinding.llTabContainer.getLayoutParams().height = height;
        return this;
    }

    public int getBottomHeight() {
        return mBinding.getRoot().getHeight();
    }
}
