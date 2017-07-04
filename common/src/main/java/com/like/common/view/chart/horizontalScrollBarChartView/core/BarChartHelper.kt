package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.RectF
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData
import com.like.logger.Logger

object BarChartHelper {
    /**
     * 获取所有柱形图的Rect。
     *
     * @param barDataList           柱形图需要的数据
     */
    fun getBarRectList(barDataList: List<BarData>): List<RectF> {
        val result: MutableList<RectF> = mutableListOf()
        if (barDataList.isNotEmpty()) {
            val maxElectricity = barDataList.maxBy { it.electricity }!!.electricity
            val eachElectricityHeight = BarChartConfig.DEFAULT_TOTAL_BAR_HEIGHT / maxElectricity
            for ((index, barData) in barDataList.withIndex()) {
                val rect = RectF()
                rect.left = index * (BarChartConfig.DEFAULT_EACH_BAR_WIDTH + BarChartConfig.DEFAULT_SPACING_BETWEEN_TWO_BARS) + BarChartConfig.DEFAULT_SPACING_BETWEEN_TWO_BARS / 2
                rect.top = BarChartConfig.DEFAULT_SPACING_BAR_TOP + BarChartConfig.DEFAULT_TOTAL_BAR_HEIGHT - barData.electricity * eachElectricityHeight
                rect.right = rect.left + BarChartConfig.DEFAULT_EACH_BAR_WIDTH
                rect.bottom = BarChartConfig.DEFAULT_SPACING_BAR_TOP + BarChartConfig.DEFAULT_TOTAL_BAR_HEIGHT
                Logger.i("month=${barData.month} index=$index top=${rect.top}")
                result.add(rect)
            }
        }
        return result
    }

}