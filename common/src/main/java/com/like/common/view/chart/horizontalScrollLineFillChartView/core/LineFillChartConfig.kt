package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.Path
import com.like.common.util.DimensionUtils
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

/**
 * 尺寸，常量
 */
class LineFillChartConfig(val context: Context) {
    companion object {
        val DEFAULT_COLORS_REAL = intArrayOf(// 柱形图颜色数组，真实数据
                0xff02bbff.toInt(),
                0xffa845e7.toInt(),
                0xffed4b90.toInt(),
                0xfff84330.toInt()
        )
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 颜色对应的终点位置的数组
                0.4f, 0.7f, 0.9f, 1.0f
        )
    }

    // 两个柱形图之间的间隔
    val spacingBetweenTwoBars: Float = DimensionUtils.dp2px(context, 30f).toFloat()
    // 柱形图高度
    val totalBarHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 柱形图距离顶部的间隔
    val spacingBarTop: Float = DimensionUtils.dp2px(context, 20f).toFloat()

    // 视图总高度
    val totalHeight = (spacingBarTop + totalBarHeight).toInt()

    // 视图总宽度
    var totalWidth = 0
    // 所有柱形图的Rect
    val pathList: MutableList<Path> = arrayListOf()
    val lineDataList: MutableList<LineData> = arrayListOf()
    fun setData(barDataList: List<LineData>) {
        this.lineDataList.clear()
        this.lineDataList.addAll(barDataList)

        totalWidth = (spacingBetweenTwoBars + spacingBetweenTwoBars * barDataList.size).toInt()

        pathList.clear()
        pathList.addAll(getAllPath())
    }

    fun getAllPath(): List<Path> {
        val result: MutableList<Path> = mutableListOf()
        if (lineDataList.isNotEmpty()) {
            val maxElectricity = lineDataList.maxBy { it.electricity }!!.electricity
            val eachElectricityHeight = totalBarHeight / maxElectricity
            for ((index, barData) in lineDataList.withIndex()) {
                val path = Path()
                if (index == 0) {
                    path.moveTo(0f, totalHeight.toFloat())
                    path.lineTo(0f, totalHeight.toFloat())
                    path.lineTo((index + 1) * spacingBetweenTwoBars, totalHeight - barData.electricity * eachElectricityHeight)
                    path.lineTo((index + 1) * spacingBetweenTwoBars, totalHeight.toFloat())
                }
                result.add(path)
            }
        }
        return result
    }

}