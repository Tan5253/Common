package com.like.common.sample

import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityPieChartBinding
import com.like.common.view.chart.pieChartView.entity.MonthData
import com.like.common.view.chart.pieChartView.entity.PieData

class PieChartActivity : BaseActivity() {
    private val mBinding: ActivityPieChartBinding by lazy {
        DataBindingUtil.setContentView<ActivityPieChartBinding>(this, R.layout.activity_pie_chart)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.root
        mBinding.viewPieChart.flEmptyViewContainer.addView(View.inflate(this, R.layout.view_pie_chart_empty_view, null))
        return null
    }

    fun changeData1(view: View) {
        mBinding.pieData = getSimulatedData1()
        mBinding.viewPieChart.flEmptyViewContainer.visibility = View.GONE
    }

    fun changeData2(view: View) {
        mBinding.pieData = getSimulatedData2()
        mBinding.viewPieChart.flEmptyViewContainer.visibility = View.GONE
    }

    fun changeData3(view: View) {
        mBinding.pieData = getSimulatedData3()
        mBinding.viewPieChart.flEmptyViewContainer.visibility = View.GONE
    }

    fun clearData(view: View) {
        mBinding.pieData = null
        mBinding.viewPieChart.flEmptyViewContainer.visibility = View.VISIBLE
    }

    fun getSimulatedData1(): PieData = PieData(2015, 1, listOf(MonthData(1, 100f, 100f), MonthData(2, 200f, 200f), MonthData(3, 300f, 300f)))

    fun getSimulatedData2(): PieData = PieData(2016, 2, listOf(MonthData(4, 400f, 400f), MonthData(5, 400f, 400f), MonthData(6, 400f, 400f)))

    fun getSimulatedData3(): PieData = PieData(2017, 3, listOf(MonthData(7, 700f, 700f), MonthData(8, 1400f, 1400f), MonthData(9, 700f, 700f)))
}