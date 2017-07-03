package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import com.like.common.util.DimensionUtils
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

/**
 * 尺寸，常量
 */
class BarChartConfig(val context: Context, val barDataList: List<BarData>) {
    companion object {
        val DEFAULT_TEXT_BG_COLOR_REAL = 0xffff0000.toInt()// 文本区域背景颜色，真实数据
        val DEFAULT_TEXT_BG_COLOR = 0xffff00ff.toInt()// 文本区域背景颜色，预测数据
        val DEFAULT_MONTH_TEXT_COLOR_REAL = 0xffffffff.toInt()// 月份数据文本颜色，真实数据
        val DEFAULT_MONTH_TEXT_COLOR = 0xcc000000.toInt()// 月份数据文本颜色，预测数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR_REAL = 0xffffffff.toInt()// 电量数据文本颜色，真实数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR = 0xff000000.toInt()// 电量数据文本颜色，预测数据
        val DEFAULT_OTHER_TEXT_COLOR = 0xffffffff.toInt()// "预测"文本
        val DEFAULT_COLORS_REAL = intArrayOf(// 柱形图颜色数组，真实数据
                0xff02bbff.toInt(),
                0xffa845e7.toInt(),
                0xffed4b90.toInt(),
                0xfff84330.toInt()
        )
        val DEFAULT_COLOR = 0xffff00ff.toInt()// 柱形图颜色，预测数据
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 颜色对应的终点位置的数组
                0.4f, 0.7f, 0.9f, 1.0f
        )
        val DEFAULT_EACH_BAR_WIDTH: Float = 35f// 每个柱形图的宽度
        val DEFAULT_TOTAL_BAR_HEIGHT: Float = 500f// 柱形图高度
        val DEFAULT_SPACING_BETWEEN_TWO_BARS: Float = 100f// 两个柱形图之间的间隔
        val DEFAULT_SPACING_ON_TEXT_TOP_OR_BOTTOM: Float = 20f// 文本区域上下留白
        val DEFAULT_TEXT_SPACING: Float = 20f// 月份数据和电量数据之间的间隙
    }

    // 视图总宽度
    val totalWidth = (DEFAULT_EACH_BAR_WIDTH * barDataList.size + DEFAULT_SPACING_BETWEEN_TWO_BARS * (barDataList.size - 1)).toInt()
    // 所有柱形图的Rect
    val barRectList: List<RectF> = BarChartHelper.getBarRectList(barDataList, DEFAULT_EACH_BAR_WIDTH, DEFAULT_TOTAL_BAR_HEIGHT, DEFAULT_SPACING_BETWEEN_TWO_BARS)
    // "预测"两个字的字体大小
    val otherTextSize = DimensionUtils.sp2px(context, 12f).toFloat()
    // 月份数据文本字体大小
    val monthTextSize = DimensionUtils.sp2px(context, 12f).toFloat()
    // 电量数据文本字体大小
    val electricityTextSize = DimensionUtils.sp2px(context, 16f).toFloat()
    // 月份数据文本绘制的起点Y坐标
    val monthTextStartY: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        BarChartConfig.DEFAULT_TOTAL_BAR_HEIGHT + getFontY(paint)
    }
    // 电量数据文本绘制的起点Y坐标
    val electricityTextStartY: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        val electricityTextTop = BarChartConfig.DEFAULT_TOTAL_BAR_HEIGHT + getFontHeight(paint)
        paint.textSize = electricityTextSize
        electricityTextTop + getFontY(paint)
    }
    // 文本区域总高度
    val totalTextHeight: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        val monthTextHeight = getFontHeight(paint)
        paint.textSize = electricityTextSize
        val electricityTextHeight = getFontHeight(paint)
        DEFAULT_SPACING_ON_TEXT_TOP_OR_BOTTOM * 2 + DEFAULT_TEXT_SPACING + monthTextHeight + electricityTextHeight
    }
    // 视图总高度
    val totalHeight = (DEFAULT_TOTAL_BAR_HEIGHT + totalTextHeight).toInt()
    // 柱形图的圆角半径
    val barRadius = DEFAULT_EACH_BAR_WIDTH / 3
    // 已出数据的文本区域背景Rect
    val textBgRect = RectF(0f, DEFAULT_TOTAL_BAR_HEIGHT, totalWidth.toFloat(), totalHeight.toFloat())

    /**
     * @return 返回指定笔和指定字符串的长度
     */
    fun getFontlength(paint: Paint, str: String) = paint.measureText(str)

    /**
     * 获取绘制文本的起点Y坐标
     */
    fun getFontY(paint: Paint) = getFontHeight(paint) / 2 + getFontLeading(paint)

    /**
     * @return 返回指定笔的文字高度
     */
    fun getFontHeight(paint: Paint) = paint.fontMetrics.descent - paint.fontMetrics.ascent

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    fun getFontLeading(paint: Paint) = paint.fontMetrics.leading - paint.fontMetrics.ascent
}