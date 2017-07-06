package com.like.common.sample

import android.databinding.DataBindingUtil
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityLineFillChartBinding
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

class LineFillChartActivity : BaseActivity() {
    private val mBinding: ActivityLineFillChartBinding by lazy {
        DataBindingUtil.setContentView<ActivityLineFillChartBinding>(this, R.layout.activity_line_fill_chart)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.lineFillView.setData(getSimulatedData())
        return null
    }

    fun getSimulatedData(): List<LineData> {
        return listOf(
                LineData(1, 1800f)
        )
    }
}