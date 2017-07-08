package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Paint
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

        val DEFAULT_SHOW_POINT_COUNT = 3// 一屏幕显示的最多点数量
    }

    // 文本字体大小
    val textSize = DimensionUtils.sp2px(context, 12f).toFloat()

    // "单位：%"顶部间隔
    val spacingUnitText1Top: Float = DimensionUtils.dp2px(context, 20f).toFloat()
    // 横坐标文本顶部的间隔
    val spacingXAxisTextTop: Float = DimensionUtils.dp2px(context, 8f).toFloat()
    // 横坐标文本底部的间隔
    val spacingXAxisTextBottom: Float = DimensionUtils.dp2px(context, 8f).toFloat()
    // 线条图的高度
    val maxLineViewHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 线条图距离顶部的间隔
    val spacingLineViewTop: Float = DimensionUtils.dp2px(context, 30f).toFloat()
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
    // "单位：%" 绘制的起点Y坐标
    val unitText1StartY: Float = spacingUnitText1Top + textBaseLine
    //  横坐标文本绘制的起点Y坐标
    val xAxisStartY: Float = spacingLineViewTop + maxLineViewHeight + spacingXAxisTextTop + textBaseLine
    // x轴下面的单位文本绘制的起点Y坐标
    val unitText2StartY: Float = spacingLineViewTop + maxLineViewHeight + spacingXAxisTextTop + textHeight + spacingXAxisTextBottom + textBaseLine
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

    fun setData(barDataList: List<TwoLineData>) {
        this.dataList.clear()
        this.dataList.addAll(barDataList)

        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        totalWidth = if (dataList.size > DEFAULT_SHOW_POINT_COUNT) {
            val eachSpacing = metric.widthPixels / (DEFAULT_SHOW_POINT_COUNT + 1)
            metric.widthPixels.toFloat() + eachSpacing * (dataList.size - DEFAULT_SHOW_POINT_COUNT)
        } else {
            metric.widthPixels.toFloat()
        }

        spacingBetweenTwoPoints = totalWidth / (dataList.size + 1)

        val maxRatio1: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio1) }!!.ratio1)
        val maxRatio2: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio2) }!!.ratio2)

        eachRatioHeight = maxLineViewHeight / 2 / maxOf(maxRatio1, maxRatio2)

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
                result.add(p)
            }
        }
        return result
    }

}