package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.RectF
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

object BarChartHelper {
    /**
     * 获取所有柱形图的Rect。
     *
     * @param barDataList           柱形图需要的数据
     */
    fun getBarRectList(barDataList: List<BarData>, barChartConfig: BarChartConfig): List<RectF> {
        val result: MutableList<RectF> = mutableListOf()
        if (barDataList.isNotEmpty()) {
            val maxElectricity = barDataList.maxBy { it.electricity }!!.electricity
            val eachElectricityHeight = barChartConfig.totalBarHeight / maxElectricity
            for ((index, barData) in barDataList.withIndex()) {
                val rect = RectF()
                rect.left = index * (barChartConfig.eachBarWidth + barChartConfig.spacingBetweenTwoBars) + barChartConfig.spacingBetweenTwoBars / 2
                rect.top = barChartConfig.spacingBarTop + barChartConfig.totalBarHeight - barData.electricity * eachElectricityHeight
                rect.right = rect.left + barChartConfig.eachBarWidth
                rect.bottom = barChartConfig.spacingBarTop + barChartConfig.totalBarHeight
                result.add(rect)
            }
        }
        return result
    }

}