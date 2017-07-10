package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.like.common.util.DimensionUtils
import com.like.common.util.DrawTextUtils
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

/**
 * 尺寸，常量
 */
class LineFillChartConfig(val context: Context) {
    companion object {
        val DEFAULT_BG_COLOR = 0xffffffff.toInt()
        val DEFAULT_COLORS = intArrayOf(// 填充颜色数组
                0xff02bbff.toInt(),
                0xff9f50fb.toInt(),
                0xffee35d6.toInt(),
                0xffff3627.toInt()
        )
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 填充颜色对应的终点位置的数组，用于按比例显示渐变
                0f, 0.33f, 0.66f, 1f
        )
        val MAX_ELECTRICITY_OF_DAY_ON_GRADIENT = 40f // (每天)渐变颜色对应的最大度数，超过此数值，为纯色DEFAULT_COLORS的最后一种颜色。
        val MAX_ELECTRICITY_OF_MONTH_ON_GRADIENT = MAX_ELECTRICITY_OF_DAY_ON_GRADIENT * 30// (每月)，渐变颜色对应的最大度数，超过此数值，为纯色DEFAULT_COLORS的最后一种颜色。
        val DEFAULT_GRADIENT_BOTTOM_BG_COLOR = 0xff02bbff.toInt()// 渐变色块以下间隔的背景颜色
        val DEFAULT_POINT_FILL_COLOR = 0xffffffff.toInt()// 点圆中间的填充颜色
        val DEFAULT_POINT_BORDER_COLOR = 0xff303030.toInt()// 点圆边框颜色
        val DEFAULT_X_AXIS_BORDER_COLOR = 0xff73d6f8.toInt()// x轴线颜色
        val DEFAULT_X_AXIS_SCALE_COLOR = 0xff73d6f8.toInt()// x轴刻度线颜色
        val DEFAULT_X_AXIS_TEXT_COLOR = 0xffffffff.toInt()// x轴文本颜色
        val DEFAULT_POINT_TEXT_COLOR = 0xff303030.toInt()// 点的数值文本颜色
        val DEFAULT_SHOW_POINT_COUNT = 3// 一屏幕显示的最多点数量
    }

    // x轴文本字体大小
    val xAxisTextSize = DimensionUtils.sp2px(context, 12f).toFloat()
    // 点的数值文本字体大小
    val pointTextSize = DimensionUtils.sp2px(context, 12f).toFloat()

    // x轴刻度线高度
    val xAxisScaleHeight: Float = DimensionUtils.dp2px(context, 8f).toFloat()
    // 点圆边框宽度
    val pointBorderWidth: Float = DimensionUtils.dp2px(context, 2f).toFloat()
    // 点圆的半径
    val pointCircleRadius: Float = DimensionUtils.dp2px(context, 2.5f).toFloat()
    // 渐变色块最高点的高度
    val maxGradientHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 渐变色块距离顶部的间隔
    val spacingGradientTop: Float = DimensionUtils.dp2px(context, 30f).toFloat()
    // 渐变色块距离底部的间隔
    val spacingGradientBottom: Float = DimensionUtils.dp2px(context, 47f).toFloat()
    // 点的数值距离点圆的间隔
    val spacingPointTextBottom = DimensionUtils.dp2px(context, 5f).toFloat()
    // x轴下面的单位上边的间隔
    val spacingUnitTextTop = DimensionUtils.dp2px(context, 8f).toFloat()
    // x轴下面的单位左边的间隔
    val spacingUnitTextLeft = DimensionUtils.dp2px(context, 12f).toFloat()

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
    // 所有数据
    val lineDataList: MutableList<LineData> = arrayListOf()
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

    fun setData(barDataList: List<LineData>) {
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