package com.like.common.view.chart.pieChartView.core

import android.content.Context
import com.like.common.util.DimensionUtils
import com.like.common.view.chart.pieChartView.entity.PieData

/**
 * 尺寸，常量
 */
class PieChartConfig(val context: Context) {
    companion object {
        val DEFAULT_BG_COLOR = 0xffffffff.toInt()
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
    // 环形宽度
    val ringWidth = DimensionUtils.dp2px(context, 40f).toFloat()

    // 每个月旋转的角度
    val sweepAngle: FloatArray = kotlin.FloatArray(3)
    // 每个月绘制的起点角度
    val startAngle: FloatArray = kotlin.FloatArray(3)

    fun setData(data: PieData) {
        val totalElectricity = data.monthDataList.sumByDouble { it.electricity.toDouble() }
        startAngle[0] = 150f
        for (i in 0..2) {
            sweepAngle[i] = 360f * data.monthDataList[i].electricity / totalElectricity.toFloat()
            if (i > 0) {
                startAngle[i] = startAngle[i - 1] + sweepAngle[i - 1]
            }
        }
    }

}