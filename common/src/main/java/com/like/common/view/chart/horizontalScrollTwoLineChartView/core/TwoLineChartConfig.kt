package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.like.common.util.DimensionUtils
import com.like.common.util.DrawTextUtils
import com.like.common.view.chart.horizontalScrollLineFillChartView.core.LineFillChartConfig.Companion.DEFAULT_SHOW_POINT_COUNT
import com.like.common.view.chart.horizontalScrollLineFillChartView.core.LineFillChartConfig.Companion.MAX_ELECTRICITY_OF_MONTH_ON_GRADIENT
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData

/**
 * 尺寸，常量
 */
class TwoLineChartConfig(val context: Context) {
    companion object {
        val DEFAULT_BG_COLOR = 0xffffffff.toInt()
        val DEFAULT_LINE_COLOR_1 = 0xff00a7ff.toInt()// 环比线颜色
        val DEFAULT_LINE_COLOR_2 = 0xffff9600.toInt()// 同比线颜色
        val DEFAULT_OTHER_LINE_COLOR = 0xffc0c0c0.toInt()// 其它线颜色

        val DEFAULT_TEXT_BG_COLOR_1 = 0xff14d13a.toInt()// 环比值背景颜色
        val DEFAULT_TEXT_BG_COLOR_2 = 0xffff3b14.toInt()// 同比值背景颜色

        val DEFAULT_TEXT_COLOR_0 = 0xff606060.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_1 = 0xffffffff.toInt()// 文本颜色
    }

    // 文本字体大小
    val xAxisTextSize = DimensionUtils.sp2px(context, 12f).toFloat()

    // 线条图最高点的高度
    val maxLineViewHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 线条图距离顶部的间隔
    val spacingLineViewTop: Float = DimensionUtils.dp2px(context, 30f).toFloat()
    // 线条图距离底部的间隔
    val spacingLineViewBottom: Float = DimensionUtils.dp2px(context, 60f).toFloat()


    // 渐变色块及其顶部间隔的总高度
    val totalGradientAndSpacingTopHeight = spacingGradientTop + maxGradientHeight
    // 视图总高度
    val totalHeight = totalGradientAndSpacingTopHeight + spacingGradientBottom

    val paint: Paint = Paint()
    // 月份数据文本绘制的起点Y坐标
    val xAxisTextStartY: Float by lazy {
        paint.textSize = xAxisTextSize
        totalGradientAndSpacingTopHeight + DrawTextUtils.getTextBaseLine(paint)
    }
    // x轴下面的单位文本绘制的起点Y坐标
    val unitTextStartY: Float by lazy {
        paint.textSize = xAxisTextSize
        val top = totalGradientAndSpacingTopHeight + DrawTextUtils.getTextHeight(paint)
        paint.textSize = unitTextSize
        top + spacingUnitTextTop + DrawTextUtils.getTextBaseLine(paint)
    }
    // 所有数据
    val lineDataList: MutableList<TwoLineData> = arrayListOf()
    // 所有点
    val pointList: MutableList<PointF> = arrayListOf()
    // 所有点的封闭路径
    val pathList: MutableList<Path> = arrayListOf()
    // 视图总宽度
    var totalWidth: Float = 0f
    // 两点之间的间隔
    var spacingBetweenTwoPoints: Float = 0f
    // 每度电量对应的高度
    var eachElectricityHeight: Float = 0f
    // LinearGradient的y1值
    var linearGradientY1: Float = spacingGradientTop
    // 渐变色块以下间隔的纯色背景
    val gradientBottomRect: RectF = RectF()

    fun setData(barDataList: List<TwoLineData>) {
        this.lineDataList.clear()
        this.lineDataList.addAll(barDataList)

        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        totalWidth = if (lineDataList.size > DEFAULT_SHOW_POINT_COUNT) {
            val eachSpacing = metric.widthPixels / (DEFAULT_SHOW_POINT_COUNT + 1)
            metric.widthPixels.toFloat() + eachSpacing * (lineDataList.size - DEFAULT_SHOW_POINT_COUNT)
        } else {
            metric.widthPixels.toFloat()
        }

        spacingBetweenTwoPoints = totalWidth / (lineDataList.size + 1)

        eachElectricityHeight = maxGradientHeight / lineDataList.maxBy { it.electricity }!!.electricity

        val gradientblockHeight = eachElectricityHeight * MAX_ELECTRICITY_OF_MONTH_ON_GRADIENT// 渐变色块的高度
        linearGradientY1 = maxGradientHeight - gradientblockHeight + spacingGradientTop

        gradientBottomRect.left = 0f
        gradientBottomRect.top = totalGradientAndSpacingTopHeight
        gradientBottomRect.right = totalWidth
        gradientBottomRect.bottom = totalHeight

        pathList.clear()
        pathList.addAll(getAllPath())

        pointList.clear()
        pointList.addAll(getAllPoint())
    }

    fun getAllPoint(): List<PointF> {
        val result: MutableList<PointF> = mutableListOf()
        if (lineDataList.isNotEmpty()) {
            for (index in 0..lineDataList.size - 1) {
                val p: PointF = PointF()
                p.x = (index + 1) * spacingBetweenTwoPoints
                p.y = totalGradientAndSpacingTopHeight - lineDataList[index].electricity * eachElectricityHeight
                result.add(p)
            }
        }
        return result
    }

    fun getAllPath(): List<Path> {
        val result: MutableList<Path> = mutableListOf()
        if (lineDataList.isNotEmpty()) {
            // 添加多余的开始，为了封闭开始
            val startPath = Path()
            startPath.moveTo(0f, totalGradientAndSpacingTopHeight)
            startPath.lineTo(spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight - lineDataList[0].electricity * eachElectricityHeight)
            startPath.lineTo(spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight)
            result.add(startPath)

            for (index in 0..lineDataList.size - 2) {
                val path = Path()
                path.moveTo((index + 1) * spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight)
                path.lineTo((index + 1) * spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight - lineDataList[index].electricity * eachElectricityHeight)
                path.lineTo((index + 2) * spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight - lineDataList[index + 1].electricity * eachElectricityHeight)
                path.lineTo((index + 2) * spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight)
                result.add(path)
            }

            // 添加多余的结束，为了封闭结束
            val endPath = Path()
            endPath.moveTo(lineDataList.size * spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight)
            endPath.lineTo(lineDataList.size * spacingBetweenTwoPoints, totalGradientAndSpacingTopHeight - lineDataList[lineDataList.size - 1].electricity * eachElectricityHeight)
            endPath.lineTo(totalWidth, totalGradientAndSpacingTopHeight)
            result.add(endPath)
        }
        return result
    }
}