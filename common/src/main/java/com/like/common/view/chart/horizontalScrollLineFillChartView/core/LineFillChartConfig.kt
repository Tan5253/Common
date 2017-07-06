package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import com.like.common.util.DimensionUtils
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

/**
 * 尺寸，常量
 */
class LineFillChartConfig(val context: Context) {
    companion object {
        val MAX_ELECTRICITY_OF_MONTH_ON_GRADIENT = 40f * 30f// 每天40度*30天，渐变颜色对应的最大度数，超过此数值，为纯色DEFAULT_COLORS的最后一种颜色。
        val DEFAULT_COLORS = intArrayOf(// 填充颜色数组
                0xff02bbff.toInt(),
                0xff9f50fb.toInt(),
                0xffee35d6.toInt(),
                0xffff3627.toInt()
        )
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 填充颜色对应的终点位置的数组，用于按比例显示渐变
                0f, 0.33f, 0.66f, 1f
        )
    }

    // 点圆的半径
    val pointCircleRadius: Float = DimensionUtils.dp2px(context, 2f).toFloat()
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
    val pointList: MutableList<PointF> = arrayListOf()
    // 渐变色块背景
    val gradientblockRect = RectF(0f, 0f, 0f, 0f)

    fun setData(barDataList: List<LineData>) {
        this.lineDataList.clear()
        this.lineDataList.addAll(barDataList)

        totalWidth = (spacingBetweenTwoPoints + spacingBetweenTwoPoints * barDataList.size).toInt()

        pathList.clear()
        pathList.addAll(getAllPath())

        pointList.clear()
        pointList.addAll(getAllPoint())

        gradientblockRect.right = totalWidth.toFloat()
        gradientblockRect.bottom = totalHeight.toFloat()
        calcGradientblockRect()
    }

    fun calcGradientblockRect() {
        if (lineDataList.isNotEmpty()) {
            val maxElectricity = lineDataList.maxBy { it.electricity }!!.electricity
            val eachElectricityHeight = maxPointHeight / maxElectricity
            val gradientblockHeight = eachElectricityHeight * MAX_ELECTRICITY_OF_MONTH_ON_GRADIENT// 渐变色块的高度
            if (maxElectricity > MAX_ELECTRICITY_OF_MONTH_ON_GRADIENT) {
                gradientblockRect.top = maxPointHeight - gradientblockHeight
            } else if (maxElectricity < MAX_ELECTRICITY_OF_MONTH_ON_GRADIENT) {
                gradientblockRect.top = maxPointHeight - gradientblockHeight
            }
        }
    }

    fun getAllPoint(): List<PointF> {
        val result: MutableList<PointF> = mutableListOf()
        if (lineDataList.isNotEmpty()) {
            val maxElectricity = lineDataList.maxBy { it.electricity }!!.electricity
            val eachElectricityHeight = maxPointHeight / maxElectricity
            for (index in 0..lineDataList.size - 1) {
                val p: PointF = PointF()
                p.x = (index + 1) * spacingBetweenTwoPoints
                p.y = totalHeight - lineDataList[index].electricity * eachElectricityHeight
                result.add(p)
            }
        }
        return result
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