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
        val DEFAULT_TEXT_BG_COLOR_REAL = 0xff28c4ff.toInt()// 文本区域背景颜色，真实数据
        val DEFAULT_TEXT_BG_COLOR = 0xffe5f8ff.toInt()// 文本区域背景颜色，预测数据
        val DEFAULT_MONTH_TEXT_COLOR_REAL = 0xffbde9ff.toInt()// 月份数据文本颜色，真实数据
        val DEFAULT_MONTH_TEXT_COLOR = 0xff9c9c9c.toInt()// 月份数据文本颜色，预测数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR_REAL = 0xffffffff.toInt()// 电量数据文本颜色，真实数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR = 0xff303030.toInt()// 电量数据文本颜色，预测数据
        val DEFAULT_OTHER_TEXT_COLOR = 0xff9c9c9c.toInt()// "预测"文本
        val DEFAULT_COLORS_REAL = intArrayOf(// 柱形图颜色数组，真实数据
                0xff02bbff.toInt(),
                0xffa845e7.toInt(),
                0xffed4b90.toInt(),
                0xfff84330.toInt()
        )
        val DEFAULT_COLOR = 0xff9c9c9c.toInt()// 柱形图颜色，预测数据
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 颜色对应的终点位置的数组
                0.4f, 0.7f, 0.9f, 1.0f
        )
        val DEFAULT_EACH_BAR_WIDTH: Float = 40f// 每个柱形图的宽度
        val DEFAULT_TOTAL_BAR_HEIGHT: Float = 500f// 柱形图高度
        val DEFAULT_SPACING_BETWEEN_TWO_BARS: Float = 90f// 两个柱形图之间的间隔
        val DEFAULT_SPACING_BETWEEN_BAR_AND_TEXT: Float = 50f// 柱形图和文本区域之间的间隔
        val DEFAULT_SPACING_BAR_TOP: Float = 50f// 柱形图距离顶部的间隔
        val DEFAULT_SPACING_ON_TEXT_TOP_OR_BOTTOM: Float = 20f// 文本区域上下留白
        val DEFAULT_TEXT_SPACING: Float = 20f// 月份数据和电量数据之间的间隙
    }

    // 视图总宽度
    val totalWidth = (DEFAULT_EACH_BAR_WIDTH * barDataList.size + DEFAULT_SPACING_BETWEEN_TWO_BARS * barDataList.size).toInt()
    // 所有柱形图的Rect
    val barRectList: List<RectF> = BarChartHelper.getBarRectList(barDataList)
    // "预测"两个字的字体大小
    val otherTextSize = DimensionUtils.sp2px(context, 10f).toFloat()
    // 月份数据文本字体大小
    val monthTextSize = DimensionUtils.sp2px(context, 10f).toFloat()
    // 电量数据文本字体大小
    val electricityTextSize = DimensionUtils.sp2px(context, 12f).toFloat()
    // 月份数据文本绘制的起点Y坐标
    val monthTextStartY: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        DEFAULT_SPACING_BAR_TOP + DEFAULT_TOTAL_BAR_HEIGHT + DEFAULT_SPACING_BETWEEN_BAR_AND_TEXT + getFontY(paint)
    }
    // 电量数据文本绘制的起点Y坐标
    val electricityTextStartY: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        val electricityTextTop = DEFAULT_SPACING_BAR_TOP + DEFAULT_TOTAL_BAR_HEIGHT + DEFAULT_SPACING_BETWEEN_BAR_AND_TEXT + getFontHeight(paint)
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
    val totalHeight = (DEFAULT_SPACING_BAR_TOP + DEFAULT_TOTAL_BAR_HEIGHT + DEFAULT_SPACING_BETWEEN_BAR_AND_TEXT + totalTextHeight).toInt()
    // 柱形图的圆角半径
    val barRadius = DEFAULT_EACH_BAR_WIDTH / 3
    // 已出数据的文本区域背景Rect
    val textBgRect = RectF(0f, DEFAULT_SPACING_BAR_TOP + DEFAULT_TOTAL_BAR_HEIGHT + DEFAULT_SPACING_BETWEEN_BAR_AND_TEXT, totalWidth.toFloat(), totalHeight.toFloat())

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