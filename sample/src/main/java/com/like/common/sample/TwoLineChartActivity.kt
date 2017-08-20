package com.like.common.sample

import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityTwoLineChartBinding
import com.like.common.util.RxBusTag
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData
import com.like.logger.Logger
import com.like.rxbus.annotations.RxBusSubscribe

class TwoLineChartActivity : BaseActivity() {
    private val mBinding: ActivityTwoLineChartBinding by lazy {
        DataBindingUtil.setContentView<ActivityTwoLineChartBinding>(this, R.layout.activity_two_line_chart)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.root
        // 测试有月份值默认值的情况
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData1(), 1)
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：日"
        return null
    }

    fun changeData1(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData1())
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：日"
    }

    fun changeData2(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData2())
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：月"
    }

    fun changeData3(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData3())
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.GONE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：时"
    }

    fun clearData(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(emptyList())
    }

    fun getSimulatedData1(): List<TwoLineData> {
        return listOf(
                TwoLineData(2, 50f, 20f),
                TwoLineData(3, 100f, 10f),
                TwoLineData(4, 10f, 50f),
                TwoLineData(5, 50f, 20f),
                TwoLineData(6, 100f, 10f),
                TwoLineData(7, 10f, 50f),
                TwoLineData(8, 50f, 20f),
                TwoLineData(9, 100f, 10f),
                TwoLineData(10, 10f, 50f)
        )
    }

    fun getSimulatedData2(): List<TwoLineData> {
        return listOf(
                TwoLineData(1, 50f, 20f),
                TwoLineData(2, 100f, -10f),
                TwoLineData(3, 10f, 0f),
                TwoLineData(4, 70f, -20f)
        )
    }

    fun getSimulatedData3(): List<TwoLineData> {
        return listOf(
                TwoLineData(1, -50f),
                TwoLineData(2, -100f),
                TwoLineData(3, -10f)
        )
    }

    @RxBusSubscribe(RxBusTag.TAG_TWO_LINE_CHART_VIEW_CLICKED)
    fun TAG_TWO_LINE_CHART_VIEW_CLICKED(touchPositon: Int) {
        Logger.e("触摸点位置：$touchPositon")
    }

}