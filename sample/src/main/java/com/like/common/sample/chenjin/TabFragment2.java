package com.like.common.sample.chenjin;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.like.base.context.BaseFragment;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.R;
import com.like.common.sample.databinding.FragmentTab2Binding;
import com.like.common.util.StatusBarUtils;
import com.like.common.view.toolbar.ToolbarUtils;

/**
 * Created by like on 2016/7/26.
 */

public class TabFragment2 extends BaseFragment {
    private FragmentTab2Binding mBinding;

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    private void initToolBar() {
        new ToolbarUtils(getActivity(), mBinding.flToolbarContainer)
                .showTitle("Tab2", R.color.common_text_white_0)
                .hideDivider()
                .setBackgroundByColorResId(R.color.common_text_blue_0);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_2, container, false);
        initToolBar();
        StatusBarUtils.setStatusBarColorForFragment((ViewGroup) mBinding.getRoot(), Color.GREEN);
        return mBinding.getRoot();
    }

}
