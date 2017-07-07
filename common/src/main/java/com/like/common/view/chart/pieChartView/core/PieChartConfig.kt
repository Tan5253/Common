package com.like.common.view.chart.pieChartView.core

import android.content.Context
import com.like.common.util.DimensionUtils
import com.like.common.view.chart.pieChartView.entity.PieData

/**
 * 尺寸，常量
 */
class PieChartConfig(val context: Context) {
    companion object {
        val DEFAULT_COLORS = intArrayOf(// 填充颜色数组
                0xffff5581.toInt(),
                0xff00ccff.toInt(),
                0xffffcc00.toInt()
        )
        val DEFAULT_TEXT_COLOR_0 = 0xff606060.toInt()// 文本颜色，"2017"
        val DEFAULT_TEXT_COLOR_1 = 0xff606060.toInt()// 文本颜色，"一季度"
    }

    // 文本字体大小，"2017"
    val textSize0 = DimensionUtils.sp2px(context, 14f).toFloat()
    // 文本字体大小，"一季度"
    val textSize1 = DimensionUtils.sp2px(context, 16f).toFloat()

    // 第一段圆饼扇形的占圆的比例
    var pieRatio0: Float = 0f
    // 第二段圆饼扇形的占圆的比例
    var pieRatio1: Float = 0f
    // 第三段圆饼扇形的占圆的比例
    var pieRatio2: Float = 0f

    fun setData(data: PieData) {
        val totalElectricity = data.monthDataList.sumByDouble { it.electricity.toDouble() }
        pieRatio0 = data.monthDataList[0].electricity / totalElectricity.toFloat()
        pieRatio1 = data.monthDataList[1].electricity / totalElectricity.toFloat()
        pieRatio2 = data.monthDataList[2].electricity / totalElectricity.toFloat()
    }

}