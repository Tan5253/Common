package com.like.common.sample

import android.databinding.DataBindingUtil
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityLineFillChartBinding

class LineFillChartActivity : BaseActivity() {
    private val mBinding: ActivityLineFillChartBinding by lazy {
        DataBindingUtil.setContentView<ActivityLineFillChartBinding>(this, R.layout.activity_line_fill_chart)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.root
        return null
    }

}