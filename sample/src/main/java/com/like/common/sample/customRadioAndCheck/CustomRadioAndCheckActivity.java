package com.like.common.sample.customRadioAndCheck;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import com.like.common.base.context.BaseActivity;
import com.like.common.base.viewmodel.BaseViewModel;
import com.like.common.sample.R;
import com.like.common.sample.databinding.ActivityCustomRadioAndCheckBinding;
import com.like.common.util.StatusBarUtils;
import com.like.common.view.check.CheckController;
import com.like.common.view.radio.RadioController;
import com.like.common.view.toolbar.ToolbarUtils;

/**
 * Created by like on 2016/12/1.
 */

public class CustomRadioAndCheckActivity extends BaseActivity {
    private ActivityCustomRadioAndCheckBinding mBinding;

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_custom_radio_and_check);
        initToolBar();
        initRadio();
        initCheck();
        // 设置沉浸式状态栏
        StatusBarUtils.setStatusBarColorForActivity((ViewGroup) mBinding.getRoot(), Color.GREEN);
    }

    private void initToolBar() {
        new ToolbarUtils(this, mBinding.flToolbarContainer)
                .showTitle("普通的单选和复选", R.color.common_text_white_0)
                .setBackgroundByColorResId(R.color.common_text_red_0)
                .showBackButton();
    }

    private void initRadio() {
        RadioController radioController = new RadioController();
        mBinding.setSelectedId(radioController.getCurSelectedId());
        mBinding.radio0.setOnClickListener(view -> radioController.select("0"));
        mBinding.radio1.setOnClickListener(view -> radioController.select("1"));
        mBinding.radio2.setOnClickListener(view -> radioController.select("2"));
        mBinding.radio3.setOnClickListener(view -> radioController.select("3"));
        mBinding.radio4.setOnClickListener(view -> radioController.select("4"));
        mBinding.radio5.setOnClickListener(view -> radioController.select("5"));
    }

    private void initCheck() {
        CheckController controller = new CheckController();
        for (int i = 0; i < 6; i++) {
            controller.add(String.valueOf(i));
        }
        mBinding.setIsChecked0(controller.get("0"));
        mBinding.setIsChecked1(controller.get("1"));
        mBinding.setIsChecked2(controller.get("2"));
        mBinding.setIsChecked3(controller.get("3"));
        mBinding.setIsChecked4(controller.get("4"));
        mBinding.setIsChecked5(controller.get("5"));
        mBinding.check0.setOnClickListener(view -> controller.select("0"));
        mBinding.check1.setOnClickListener(view -> controller.select("1"));
        mBinding.check2.setOnClickListener(view -> controller.select("2"));
        mBinding.check3.setOnClickListener(view -> controller.select("3"));
        mBinding.check4.setOnClickListener(view -> controller.select("4"));
        mBinding.check5.setOnClickListener(view -> controller.select("5"));
    }
}
