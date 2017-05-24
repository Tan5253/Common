package com.like.common.sample.objectbox;

import android.databinding.DataBindingUtil;

import com.like.base.context.BaseActivity;
import com.like.base.entity.Host;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.R;
import com.like.common.sample.databinding.ActivityObjectBoxBinding;
import com.like.common.view.toolbar.ToolbarUtils;

/**
 * ObjectBox数据库测试
 *
 * @author like
 * @version 1.0
 * @created at 2017/5/14 11:14
 */

public class ObjectBoxActivity extends BaseActivity {
    private ActivityObjectBoxBinding mBinding;

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_object_box);
        ObjectBoxViewModel viewModel = new ObjectBoxViewModel(new Host(this));
        mBinding.setViewModel(viewModel);
        initToolBar();
        return viewModel;
    }

    private void initToolBar() {
        new ToolbarUtils(this, mBinding.flToolbarContainer)
                .showTitle("ObjectBox数据库测试", R.color.common_text_white_0)
                .showBackButton();
    }

}
