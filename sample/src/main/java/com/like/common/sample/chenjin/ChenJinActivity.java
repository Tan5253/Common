package com.like.common.sample.chenjin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.like.base.context.BaseActivity;
import com.like.base.entity.Host;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.R;
import com.like.common.sample.databinding.ActivityChenjinBinding;
import com.like.common.util.ResourceUtils;
import com.like.common.util.StatusBarUtils;
import com.like.common.view.bottomNavigationBars.BottomNavigationBarsHelper;

import java.util.List;

public class ChenJinActivity extends BaseActivity {
    private ActivityChenjinBinding mBinding;

    private BottomNavigationBarsHelper mBottomNavigationBarsHelper;

    public static int mCurPagerIndex;
    private ChenJinViewModel mViewModel;

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chenjin);
        mViewModel = new ChenJinViewModel(new Host(this), mBinding);
        return mViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.vp.setOffscreenPageLimit(3);
        addBottomTabView();
        StatusBarUtils.setStatusBarTranslucent(this);
    }

    @Override
    public void onBackPressed() {
        // 当处于主页面时，非tab0就返回tab0。当处于其他页面时，就返回主页面。
        if (mBinding.vp.getCurrentItem() != 0) {
            mBottomNavigationBarsHelper.selectByPos(0);// 相当于tab视图回到初始状态
            return;
        }
        super.onBackPressed();
    }

    public void setMessageCount(int count) {
        mBottomNavigationBarsHelper.setMessageCount(1, count);
    }

    /**
     * 添加底部tab导航栏视图
     */
    private void addBottomTabView() {
        mBottomNavigationBarsHelper = new BottomNavigationBarsHelper(this);

        // 设置bottom的背景
        mBottomNavigationBarsHelper.setBottomBgColor(ResourceUtils.getColor(this, R.color.main_bottom_tab_bg));

        // 添加原始的
        String[] names = {"Tab0", "Tab1", "Tab2"};
        int[] originNormalImageResIds = {R.drawable.main_bottom_tab0_normal, R.drawable.main_bottom_tab1_normal, R.drawable.main_bottom_tab2_normal};
        int[] originPressImageResIds = {R.drawable.main_bottom_tab0_press, R.drawable.main_bottom_tab1_press, R.drawable.main_bottom_tab2_press};
        int normalTextColor = R.color.main_bottom_tab_bg_normal;
        int pressTextColor = R.color.main_bottom_tab_bg_press;
        mBottomNavigationBarsHelper.initOriginView(names, originNormalImageResIds, originPressImageResIds, normalTextColor, pressTextColor);
        mBinding.flBottomTabContainer.addView(mBottomNavigationBarsHelper.getView());

        mBottomNavigationBarsHelper.setTabSelectedListener(selectedIndex -> {
            mCurPagerIndex = selectedIndex;
            mBinding.vp.setCurrentItem(selectedIndex, false);
        });
    }

    public List<Fragment> getAllFragment() {
        return mViewModel.getAllFragment();
    }

}
