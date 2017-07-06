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
        val DEFAULT_COLORS = intArrayOf(// 填充颜色数组
                0xff02bbff.toInt(),
                0xffa845e7.toInt(),
                0xffed4b90.toInt(),
                0xfff84330.toInt()
        )
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 填充颜色对应的终点位置的数组，用于按比例显示渐变
                0.4f, 0.7f, 0.9f, 1.0f
        )
    }

    // 两点之间的间隔
    val spacingBetweenTwoPoints: Float = DimensionUtils.dp2px(context, 30f).toFloat()
    // 最高的点的高度
    val maxPointHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 最高的点距离顶部的间隔
    val spacingPointTop: Float = DimensionUtils.dp2px(context, 20f).toFloat()

    // 视图总高度
    val totalHeight = (spacingPointTop + maxPointHeight).toInt()

    // 视图总宽度
    var totalWidth = 0
    // 所有点的形成的封闭路径
    val pathList: MutableList<Path> = arrayListOf()
    val lineDataList: MutableList<LineData> = arrayListOf()
    fun setData(barDataList: List<LineData>) {
        this.lineDataList.clear()
        this.lineDataList.addAll(barDataList)

        totalWidth = (spacingBetweenTwoPoints + spacingBetweenTwoPoints * barDataList.size).toInt()

        pathList.clear()
        pathList.addAll(getAllPath())
    }

    fun getAllPath(): List<Path> {
        val result: MutableList<Path> = mutableListOf()
        if (lineDataList.isNotEmpty()) {
            val maxElectricity = lineDataList.maxBy { it.electricity }!!.electricity
            val eachElectricityHeight = maxPointHeight / maxElectricity

            // 添加多余的开始，为了封闭开始
            val startPath = Path()
            startPath.moveTo(0f, totalHeight.toFloat())
            startPath.lineTo(spacingBetweenTwoPoints, totalHeight - lineDataList[0].electricity * eachElectricityHeight)
            startPath.lineTo(spacingBetweenTwoPoints, totalHeight.toFloat())
            result.add(startPath)

            for (index in 0..lineDataList.size - 2) {
                val path = Path()
                path.moveTo((index + 1) * spacingBetweenTwoPoints, totalHeight.toFloat())
                path.lineTo((index + 1) * spacingBetweenTwoPoints, totalHeight - lineDataList[index].electricity * eachElectricityHeight)
                path.lineTo((index + 2) * spacingBetweenTwoPoints, totalHeight - lineDataList[index + 1].electricity * eachElectricityHeight)
                path.lineTo((index + 2) * spacingBetweenTwoPoints, totalHeight.toFloat())
                result.add(path)
            }

            // 添加多余的结束，为了封闭结束
            val endPath = Path()
            endPath.moveTo(lineDataList.size * spacingBetweenTwoPoints, totalHeight.toFloat())
            endPath.lineTo(lineDataList.size * spacingBetweenTwoPoints, totalHeight - lineDataList[lineDataList.size - 1].electricity * eachElectricityHeight)
            endPath.lineTo(totalWidth.toFloat(), totalHeight.toFloat())
            result.add(endPath)
        }
        return result
    }

}