package com.like.common.view.bottomNavigationBars;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import com.like.common.R;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBarsHelper {
    private Activity mActivity;
    private View mView;

    private List<BottomTabInfo> mTabInfoList;

    private LinearLayout ll_bg;
    private LinearLayout ll_container_new;
    private LinearLayout ll_container_origin;

    private OnTabSelectedListener mTabSelectedForViewPager;

    private int mCurPosition;

    /**
     * 当某个tab被选中的监听，用于本类中处理反选问题
     */
    private OnTabSelectedListener mTabSelectedListener = new OnTabSelectedListener() {
        @Override
        public void onSelected(int selectedIndex) {
            // 通知Activity改变ViewPager页面
            if (mTabSelectedForViewPager != null) {
                mTabSelectedForViewPager.onSelected(selectedIndex);
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

    public BottomNavigationBarsHelper(Activity activity) {
        mActivity = activity;
        mView = View.inflate(mActivity, R.layout.view_activity_bottom_tab, null);
        ll_bg = (LinearLayout) mView.findViewById(R.id.ll_bg);
    }

    public void initOriginView(String[] names, int[] normalImageResIds, int[] pressImageResIds, int normalTextColorResId, int pressTextColorResId) {
        if (names == null || normalImageResIds == null || pressImageResIds == null || normalTextColorResId < 0 || pressTextColorResId < 0) {
            throw new IllegalArgumentException("参数错误");
        }
        if (names.length < 3 || names.length > 5) {
            throw new IllegalArgumentException("tab的数量必须在3-5个");
        }
        if (names.length != normalImageResIds.length || names.length != pressImageResIds.length) {
            throw new IllegalArgumentException("参数错误");
        }

        ll_container_origin = (LinearLayout) mView.findViewById(R.id.ll_container_origin);

        if (ll_container_origin == null) {
            return;
        }
        ll_container_origin.removeAllViews();

        mTabInfoList = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            BottomTabInfoOrigin tabInfo = new BottomTabInfoOrigin(mActivity, i, mTabSelectedListener);
            // 设置图标
            tabInfo.setImageNormal(normalImageResIds[i]);
            tabInfo.setImagePress(pressImageResIds[i]);
            // 设置文字
            tabInfo.setTextColorNormalByResId(normalTextColorResId);
            tabInfo.setTextColorPressByResId(pressTextColorResId);

            tabInfo.initView(names[i]);

            ll_container_origin.addView(tabInfo.getView());

            mTabInfoList.add(tabInfo);
        }
        ll_container_origin.setVisibility(View.VISIBLE);
        selectByPos(mCurPosition);
    }

    /**
     * 添加活动时，会覆盖origin
     *
     * @param normalImageResIds
     * @param pressImageResIds
     */
    public void initNewView(int[] normalImageResIds, int[] pressImageResIds) {
        if (normalImageResIds == null || pressImageResIds == null) {
            throw new IllegalArgumentException("参数错误");
        }
        if (normalImageResIds.length != pressImageResIds.length || normalImageResIds.length != mTabInfoList.size()) {
            throw new IllegalArgumentException("参数错误");
        }

        ll_container_new = (LinearLayout) mView.findViewById(R.id.ll_container_new);

        if (ll_container_new == null) {
            return;
        }
        ll_container_new.removeAllViews();

        mTabInfoList = new ArrayList<>();
        for (int i = 0; i < normalImageResIds.length; i++) {
            BottomTabInfoNew tabInfo = new BottomTabInfoNew(mActivity, i, mTabSelectedListener);
            // 设置图标
            tabInfo.setImageNormal(normalImageResIds[i]);
            tabInfo.setImagePress(pressImageResIds[i]);

            tabInfo.initView();

            ll_container_new.addView(tabInfo.getView());

            mTabInfoList.add(tabInfo);
        }

        ll_container_new.setVisibility(View.VISIBLE);
        if (ll_container_origin != null)
            ll_container_origin.setVisibility(View.INVISIBLE);

        selectByPos(mCurPosition);
    }

    /**
     * 设置底部导航栏的背景图片
     *
     * @param bottomBgImage
     */
    public BottomNavigationBarsHelper setBottomBgImage(Drawable bottomBgImage) {
        if (ll_bg != null && bottomBgImage != null)
            ll_bg.setBackgroundDrawable(bottomBgImage);
        return this;
    }

    /**
     * 设置底部导航栏的背景色
     *
     * @param bottomBgColor
     */
    public BottomNavigationBarsHelper setBottomBgColor(int bottomBgColor) {
        if (ll_bg != null)
            ll_bg.setBackgroundColor(bottomBgColor);
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

    public void setTabSelectedListener(OnTabSelectedListener listener) {
        mTabSelectedForViewPager = listener;
    }

    /**
     * 获取底部tab导航视图
     *
     * @return
     */
    public View getView() {
        return mView;
    }

    /**
     * 设置消息提示数量
     *
     * @param index tab的索引，从0开始
     * @param count 消息数量 小于等于0时隐藏
     */
    public void setMessageCount(int index, int count) {
        if (index < mTabInfoList.size())
            mTabInfoList.get(index).setMessageCount(count);
    }

}
