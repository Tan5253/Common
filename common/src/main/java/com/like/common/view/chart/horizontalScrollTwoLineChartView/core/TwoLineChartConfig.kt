package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.like.common.util.DimensionUtils
import com.like.common.util.DrawTextUtils
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
        val DEFAULT_TEXT_COLOR_2 = 0xffc0c0c0.toInt()// 文本颜色

        val DEFAULT_SHOW_POINT_COUNT = 3// 一屏幕显示的最多点数量
    }

    // 文本字体大小
    val textSize = DimensionUtils.sp2px(context, 12f).toFloat()

    // 横坐标文本顶部的间隔
    val spacingXAxisTextTop: Float = DimensionUtils.dp2px(context, 8f).toFloat()
    // 线条图的高度
    val maxLineViewHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 线条图距离顶部的间隔
    val spacingLineViewTop: Float = DimensionUtils.dp2px(context, 40f).toFloat()
    // 线条图距离底部的间隔
    val spacingLineViewBottom: Float = DimensionUtils.dp2px(context, 60f).toFloat()
    // 点圆半径
    val pointCircleRadius: Float = DimensionUtils.dp2px(context, 2.5f).toFloat()

    // 视图总高度
    val totalHeight = spacingLineViewTop + maxLineViewHeight + spacingLineViewBottom

    val textBaseLine: Float by lazy {
        val paint = Paint()
        paint.textSize = textSize
        DrawTextUtils.getTextBaseLine(paint)
    }
    val textHeight: Float by lazy {
        val paint = Paint()
        paint.textSize = textSize
        DrawTextUtils.getTextHeight(paint)
    }
    // "0.00%" 绘制的起点Y坐标
    val middleLineTextStartY: Float = spacingLineViewTop + maxLineViewHeight / 2 - textHeight / 2 + textBaseLine
    //  横坐标文本绘制的起点Y坐标
    val xAxisStartY: Float = spacingLineViewTop + maxLineViewHeight + spacingXAxisTextTop + textBaseLine
    // 所有数据
    val dataList: MutableList<TwoLineData> = arrayListOf()
    // 环比对应的所有点的坐标
    val pointList1: MutableList<PointF> = arrayListOf()
    // 同比对应的所有点的坐标
    val pointList2: MutableList<PointF> = arrayListOf()
    // 视图总宽度
    var totalWidth: Float = 0f
    // 两点之间的间隔
    var spacingBetweenTwoPoints: Float = 0f
    // 每个百分比(1%)对应的高度
    var eachRatioHeight: Float = 0f
    val screenWidthPixels: Float by lazy {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        metric.widthPixels.toFloat()
    }

    // 环比path
    val path1: Path = Path()
    // 同比path
    val path2: Path = Path()

    fun setData(barDataList: List<TwoLineData>) {
        this.dataList.clear()
        this.dataList.addAll(barDataList)

        totalWidth = if (dataList.size > DEFAULT_SHOW_POINT_COUNT) {
            val eachSpacing = screenWidthPixels / (DEFAULT_SHOW_POINT_COUNT + 1)
            screenWidthPixels + eachSpacing * (dataList.size - DEFAULT_SHOW_POINT_COUNT)
        } else {
            screenWidthPixels
        }

        spacingBetweenTwoPoints = totalWidth / (dataList.size + 1)

        val maxRatio1: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio1) }!!.ratio1)
        val maxRatio2: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio2) }!!.ratio2)

        eachRatioHeight = maxLineViewHeight / 2 / maxOf(maxRatio1, maxRatio2)

        path1.reset()
        path2.reset()

        pointList1.clear()
        pointList1.addAll(getAllPoint(1))

        pointList2.clear()
        pointList2.addAll(getAllPoint(2))

    }

    fun getAllPoint(flag: Int): List<PointF> {
        val result: MutableList<PointF> = mutableListOf()
        if (dataList.isNotEmpty()) {
            for ((index, twoLineData) in dataList.withIndex()) {
                val p: PointF = PointF()
                p.x = (index + 1) * spacingBetweenTwoPoints
                p.y = if (flag == 1) {
                    spacingLineViewTop + maxLineViewHeight / 2 - twoLineData.ratio1 * eachRatioHeight
                } else if (flag == 2) {
                    spacingLineViewTop + maxLineViewHeight / 2 - twoLineData.ratio2 * eachRatioHeight
                } else {
                    0f
                }
                if (flag == 1) {
                    if (index == 0) {
                        path1.moveTo(p.x, p.y)
                    } else {
                        path1.lineTo(p.x, p.y)
                    }
                } else if (flag == 2) {
                    if (index == 0) {
                        path2.moveTo(p.x, p.y)
                    } else {
                        path2.lineTo(p.x, p.y)
                    }
                }
                result.add(p)
            }
        }
        return result
    }

}