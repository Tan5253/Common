package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import com.like.common.util.DimensionUtils
import com.like.common.util.DrawTextUtils
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

/**
 * 尺寸，常量
 */
class BarChartConfig(val context: Context) {
    companion object {
        val DEFAULT_BG_COLOR = 0xffffffff.toInt()
        val DEFAULT_COLORS_REAL = intArrayOf(// 柱形图颜色数组
                0xff02bbff.toInt(),
                0xff9f50fb.toInt(),
                0xffee35d6.toInt(),
                0xffff3627.toInt()
        )
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 柱形图颜色对应的终点位置的数组，用于按比例显示渐变
                0f, 0.33f, 0.66f, 1f
        )
        val MAX_ELECTRICITY_OF_DAY_ON_GRADIENT = 40f // (每天)渐变颜色对应的最大度数，超过此数值，为纯色DEFAULT_COLORS的最后一种颜色。
        val DEFAULT_UNIT_TEXT_COLOR = 0xffffffff.toInt()// 文本区域最左边单位的文字颜色
        val DEFAULT_UNIT_BG_COLOR = 0xff3d86b5.toInt()// 文本区域最左边单位区域的背景颜色
        val DEFAULT_TEXT_AREA_BG_COLOR_REAL = 0xff28c4ff.toInt()// 文本区域背景颜色，真实数据
        val DEFAULT_TEXT_AREA_BG_COLOR = 0xffe5f8ff.toInt()// 文本区域背景颜色，预测数据
        val DEFAULT_MONTH_TEXT_COLOR_REAL = 0xffbde9ff.toInt()// 月份数据文本颜色，真实数据
        val DEFAULT_MONTH_TEXT_COLOR = 0xff9c9c9c.toInt()// 月份数据文本颜色，预测数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR_REAL = 0xffffffff.toInt()// 电量数据文本颜色，真实数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR = 0xff303030.toInt()// 电量数据文本颜色，预测数据
        val DEFAULT_OTHER_TEXT_COLOR = 0xff9c9c9c.toInt()// "预测"文本
        val DEFAULT_COLOR = 0xff9c9c9c.toInt()// 柱形图颜色，预测数据
    }

    // "预测"两个字的字体大小
    val otherTextSize = DimensionUtils.sp2px(context, 10f).toFloat()
    // 月份数据文本字体大小
    val monthTextSize = DimensionUtils.sp2px(context, 10f).toFloat()
    // 电量数据文本字体大小
    val electricityTextSize = DimensionUtils.sp2px(context, 12f).toFloat()
    // 最左边的单位文本字体大小
    val unitTextSize = DimensionUtils.sp2px(context, 10f).toFloat()

    // 每个柱形图的宽度
    val eachBarWidth: Float = DimensionUtils.dp2px(context, 15f).toFloat()
    // 两个柱形图之间的间隔
    val spacingBetweenTwoBars: Float = DimensionUtils.dp2px(context, 30f).toFloat()
    // 最高的柱形图的高度
    val maxBarHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 柱形图和文本区域之间的间隔
    val spacingBarBottom: Float = DimensionUtils.dp2px(context, 20f).toFloat()
    // 最高的柱形图距离顶部的间隔
    val spacingBarTop: Float = DimensionUtils.dp2px(context, 30f).toFloat()
    // 文本区域上下留白
    val spacingOnTextAreaTopOrBottom: Float = DimensionUtils.dp2px(context, 8f).toFloat()
    // 月份数据和电量数据之间的间隙
    val spacingBetweenTwoText: Float = DimensionUtils.dp2px(context, 3f).toFloat()

    val paint: Paint = Paint()
    // 文本区域总高度
    val totalTextAreaHeight: Float by lazy {
        paint.textSize = monthTextSize
        val monthTextHeight = DrawTextUtils.getTextHeight(paint)
        paint.textSize = electricityTextSize
        val electricityTextHeight = DrawTextUtils.getTextHeight(paint)
        spacingOnTextAreaTopOrBottom * 2 + spacingBetweenTwoText + monthTextHeight + electricityTextHeight
    }
    // 文本区域的顶部y坐标
    val textAreaTop: Float = spacingBarTop + maxBarHeight + spacingBarBottom
    // 月份数据文本绘制的起点Y坐标
    val monthTextStartY: Float by lazy {
        paint.textSize = monthTextSize
        textAreaTop + spacingOnTextAreaTopOrBottom + DrawTextUtils.getTextBaseLine(paint)
    }
    // 电量数据文本绘制的起点Y坐标
    val electricityTextStartY: Float by lazy {
        paint.textSize = monthTextSize
        val electricityTextTop = textAreaTop + spacingOnTextAreaTopOrBottom + DrawTextUtils.getTextHeight(paint) + spacingBetweenTwoText
        paint.textSize = electricityTextSize
        electricityTextTop + DrawTextUtils.getTextBaseLine(paint)
    }
    // 每度电量对应的高度
    var eachElectricityHeight: Float = 0f
    // LinearGradient的y1值
    var linearGradientY1: Float = spacingBarTop
    // 柱形图的圆角半径
    val barRadius = eachBarWidth / 3
    // 视图总高度
    val totalHeight = (textAreaTop + totalTextAreaHeight).toInt()

    // 视图总宽度
    var totalWidth = 0
    // 已出数据的文本区域背景Rect
    val textAreaBgRect = RectF()
    // 所有柱形图的Rect
    val barRectList: MutableList<RectF> = arrayListOf()
    val barDataList: MutableList<BarData> = arrayListOf()
    fun setData(barDataList: List<BarData>) {
        this.barDataList.clear()
        this.barDataList.addAll(barDataList)

        totalWidth = (spacingBetweenTwoBars + eachBarWidth * barDataList.size + spacingBetweenTwoBars * barDataList.size).toInt()

        eachElectricityHeight = maxBarHeight / barDataList.maxBy { it.yData }!!.yData

        val gradientblockHeight = eachElectricityHeight * MAX_ELECTRICITY_OF_DAY_ON_GRADIENT// 柱形图的高度
        linearGradientY1 = maxBarHeight - gradientblockHeight + spacingBarTop

        textAreaBgRect.left = 0f
        textAreaBgRect.top = textAreaTop
        textAreaBgRect.right = totalWidth.toFloat()
        textAreaBgRect.bottom = totalHeight.toFloat()

        barRectList.clear()
        barRectList.addAll(getAllBarRect())

    }

    fun getAllBarRect(): List<RectF> {
        val result: MutableList<RectF> = mutableListOf()
        for ((index, barData) in barDataList.withIndex()) {
            val rect = RectF()
            rect.left = spacingBetweenTwoBars + index * (eachBarWidth + spacingBetweenTwoBars) + spacingBetweenTwoBars / 2
            rect.top = spacingBarTop + maxBarHeight - barData.yData * eachElectricityHeight
            rect.right = rect.left + eachBarWidth
            rect.bottom = spacingBarTop + maxBarHeight
            result.add(rect)
        }
        return result
    }

}