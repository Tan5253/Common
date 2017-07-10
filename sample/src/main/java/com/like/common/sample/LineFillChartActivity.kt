package com.like.common.sample

import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityLineFillChartBinding
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

class LineFillChartActivity : BaseActivity() {
    private val mBinding: ActivityLineFillChartBinding by lazy {
        DataBindingUtil.setContentView<ActivityLineFillChartBinding>(this, R.layout.activity_line_fill_chart)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.root
        return null
    }

    fun changeData1(view: View) {
        mBinding.viewLineFillChart.lineFillChartView.setData(getSimulatedData1())
    }

    fun changeData2(view: View) {
        mBinding.viewLineFillChart.lineFillChartView.setData(getSimulatedData2())
    }

    fun changeData3(view: View) {
        mBinding.viewLineFillChart.lineFillChartView.setData(getSimulatedData3())
    }

    fun clearData(view: View) {
        mBinding.viewLineFillChart.lineFillChartView.setData(emptyList())
    }

    fun getSimulatedData1(): List<LineData> {
        return listOf(
                LineData(1, 1000f),
                LineData(2, 1200f)
        )
    }

    fun getSimulatedData2(): List<LineData> {
        return listOf(
                LineData(1, 1000f),
                LineData(2, 2400f),
                LineData(3, 900f),
                LineData(4, 100f),
                LineData(5, 800f)
        )
    }

    fun getSimulatedData3(): List<LineData> {
        return listOf(
                LineData(1, 100f),
                LineData(2, 300f),
                LineData(3, 90f)
        )
    }
}