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
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData1(), 5)
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.llTongbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：日"
        return null
    }

    fun changeData1(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData1())
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.llTongbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：日"
    }

    fun changeData2(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData2())
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.llTongbi.visibility = View.GONE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：月"
    }

    fun changeData3(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(getSimulatedData3())
        mBinding.viewTwoLineChart.llHuanbi.visibility = View.GONE
        mBinding.viewTwoLineChart.llTongbi.visibility = View.VISIBLE
        mBinding.viewTwoLineChart.tvUnit.text = "单位：时"
    }

    fun clearData(view: View) {
        mBinding.viewTwoLineChart.twoLineChartView.setData(emptyList())
    }

    fun getSimulatedData1(): List<TwoLineData> {
//        return listOf(
//                TwoLineData(1, 100f, 0f),
//                TwoLineData(2, 50f, 20f),
//                TwoLineData(3, 100f, 10f),
//                TwoLineData(4, 10f, 50f),
//                TwoLineData(5, 50f, 20f),
//                TwoLineData(6, 100f, 10f)
//        )
//        return listOf(
//                TwoLineData(1, ratio2 = 0f),
//                TwoLineData(2, ratio2 = 20f),
//                TwoLineData(3, ratio2 = 10f),
//                TwoLineData(4, ratio2 = 50f),
//                TwoLineData(5, ratio2 = 20f),
//                TwoLineData(6, ratio2 = 10f)
//        )
        return listOf(
                TwoLineData(1, ratio2 = 0f),
                TwoLineData(2, ratio2 = 0f),
                TwoLineData(3, ratio2 = 0f),
                TwoLineData(4, ratio2 = 0f),
                TwoLineData(5, ratio2 = 0f),
                TwoLineData(6, ratio2 = 0f)
        )
//        return listOf(
//                TwoLineData(1, 0f),
//                TwoLineData(2, 0f),
//                TwoLineData(3, 0f),
//                TwoLineData(4, 0f),
//                TwoLineData(5, 0f),
//                TwoLineData(6, 0f)
//        )
//        return listOf(
//                TwoLineData(1, -10f),
//                TwoLineData(2, 20f),
//                TwoLineData(3, -30f),
//                TwoLineData(4, 15f),
//                TwoLineData(5, 50f),
//                TwoLineData(6, -20f)
//        )
    }

    fun getSimulatedData2(): List<TwoLineData> {
        return listOf(
                TwoLineData(1, ratio2 = 20f),
                TwoLineData(2, ratio2 = -10f),
                TwoLineData(3, ratio2 = 0f),
                TwoLineData(4, ratio2 = -20f)
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