package com.like.common.sample

import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityBarChartBinding
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

class BarChartActivity : BaseActivity() {
    private val mBinding: ActivityBarChartBinding by lazy {
        DataBindingUtil.setContentView<ActivityBarChartBinding>(this, R.layout.activity_bar_chart)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.root
        return null
    }

    fun changeData1(view: View) {
        mBinding.barView.setData(getSimulatedData1())
    }

    fun changeData2(view: View) {
        mBinding.barView.setData(getSimulatedData2())
    }

    fun changeData3(view: View) {
        mBinding.barView.setData(getSimulatedData3())
    }

    fun clearData(view: View) {
        mBinding.barView.setData(emptyList())
    }

    fun getSimulatedData1(): List<BarData> {
        return listOf(
                BarData(0, 40f),
                BarData(1, 80f),
                BarData(2, 30f),
                BarData(9, 9.9f, false),
                BarData(10, 11.0f, false),
                BarData(11, 12.1f, false)
        )
    }

    fun getSimulatedData2(): List<BarData> {
        return listOf(
                BarData(0, 7.5f),
                BarData(1, 13.1f),
                BarData(2, 40f),
                BarData(3, 9.9f, false),
                BarData(4, 11.0f, false),
                BarData(5, 12.1f, false)
        )
    }

    fun getSimulatedData3(): List<BarData> {
        return listOf(
                BarData(0, 7.5f),
                BarData(1, 13.1f),
                BarData(2, 2.2f),
                BarData(3, 3.3f),
                BarData(4, 8.4f),
                BarData(5, 5.5f),
                BarData(6, 6.6f),
                BarData(7, 0.5f),
                BarData(8, 8.8f),
                BarData(9, 9.9f),
                BarData(10, 11.0f),
                BarData(11, 12.1f),
                BarData(12, 13.1f),
                BarData(13, 1.1f),
                BarData(14, 2.2f),
                BarData(15, 3.3f),
                BarData(16, 4.4f),
                BarData(17, 5.5f),
                BarData(18, 6.6f),
                BarData(19, 12.1f, false),
                BarData(20, 3.8f, false),
                BarData(21, 9.9f, false),
                BarData(22, 6.0f, false),
                BarData(23, 13.1f, false)
        )
    }
}