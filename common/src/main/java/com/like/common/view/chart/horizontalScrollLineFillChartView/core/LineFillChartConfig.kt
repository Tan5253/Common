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

            // 添加多余的开始，为了封闭开始
            val startPath = Path()
            startPath.moveTo(0f, totalHeight.toFloat())
            startPath.lineTo(spacingBetweenTwoBars, totalHeight - lineDataList[0].electricity * eachElectricityHeight)
            startPath.lineTo(spacingBetweenTwoBars, totalHeight.toFloat())
            result.add(startPath)

            for (index in 0..lineDataList.size - 2) {
                val path = Path()
                path.moveTo((index + 1) * spacingBetweenTwoBars, totalHeight.toFloat())
                path.lineTo((index + 1) * spacingBetweenTwoBars, totalHeight - lineDataList[index].electricity * eachElectricityHeight)
                path.lineTo((index + 2) * spacingBetweenTwoBars, totalHeight - lineDataList[index + 1].electricity * eachElectricityHeight)
                path.lineTo((index + 2) * spacingBetweenTwoBars, totalHeight.toFloat())
                result.add(path)
            }

            // 添加多余的结束，为了封闭结束
            val endPath = Path()
            endPath.moveTo(lineDataList.size * spacingBetweenTwoBars, totalHeight.toFloat())
            endPath.lineTo(lineDataList.size * spacingBetweenTwoBars, totalHeight - lineDataList[lineDataList.size - 1].electricity * eachElectricityHeight)
            endPath.lineTo(totalWidth.toFloat(), totalHeight.toFloat())
            result.add(endPath)
        }
        return result
    }

}