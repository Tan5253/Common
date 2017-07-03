package com.like.common.sample

import android.databinding.DataBindingUtil
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityChartBinding

class ChartActivity : BaseActivity() {
    private val mBinding: ActivityChartBinding by lazy {
        DataBindingUtil.setContentView<ActivityChartBinding>(this, R.layout.activity_chart)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.root
        return null
    }
}