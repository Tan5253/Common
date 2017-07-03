package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.RectF
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

object BarChartHelper {
    /**
     * 获取柱形图的Rect。
     *
     * @param barDataList           柱形图需要的数据
     * @param eachBarWidth          每个柱形图的宽度
     * @param totalBarHeight        柱形图的总高度
     * @param spacingBetweenTwoBars 两个柱形图之间的间隔
     */
    fun getRectData(barDataList: List<BarData>, eachBarWidth: Float, totalBarHeight: Float, spacingBetweenTwoBars: Float): List<RectF> {
        val result: MutableList<RectF> = mutableListOf()
        if (barDataList.isNotEmpty()) {
            val maxElectricity = barDataList.maxBy { it.electricity }!!.electricity
            val eachElectricityHeight = totalBarHeight / maxElectricity
            for ((index, barData) in barDataList.withIndex()) {
                val rect = RectF()
                rect.left = index * (eachBarWidth + spacingBetweenTwoBars)
                rect.top = totalBarHeight - barData.electricity * eachElectricityHeight
                rect.right = rect.left + eachBarWidth
                rect.bottom = totalBarHeight
                result.add(rect)
            }
        }
        return result
    }

}