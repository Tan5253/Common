package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.RectF
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

/**
 * 尺寸，常量
 */
class BarChartConfig(val barDataList: List<BarData>) {
    companion object {
        val DEFAULT_TEXT_BG_COLOR = 0xffff0000.toInt()// 文本区域背景颜色
        val DEFAULT_MONTH_TEXT_COLOR = 0xffffffff.toInt()// 月份数据文本颜色
        val DEFAULT_ELECTRICITY_TEXT_COLOR = 0xffffffff.toInt()// 电量数据文本颜色
        val DEFAULT_COLORS = intArrayOf(// 颜色数组
                0xff02bbff.toInt(),
                0xffa845e7.toInt(),
                0xffed4b90.toInt(),
                0xfff84330.toInt()
        )
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 颜色对应的终点位置的数组
                0.4f, 0.7f, 0.9f, 1.0f
        )
        val DEFAULT_EACH_BAR_WIDTH: Float = 35f// 每个柱形图的宽度
        val DEFAULT_TOTAL_BAR_HEIGHT: Float = 500f// 柱形图高度
        val DEFAULT_TOTAL_TEXT_HEIGHT: Int = 110// 文本区域总高度
        val DEFAULT_SPACING_BETWEEN_TWO_BARS: Float = 100f// 两个柱形图之间的间隔
        val DEFAULT_SPACING_ON_TEXT_TOP_OR_BOTTOM: Float = 20f// 文本区域上下留白
        val DEFAULT_TEXT_SPACING: Float = 20f// 月份数据和电量数据之间的间隙
    }

    // 视图总宽度
    val totalWidth = (DEFAULT_EACH_BAR_WIDTH * barDataList.size + DEFAULT_SPACING_BETWEEN_TWO_BARS * (barDataList.size - 1)).toInt()
    // 所有柱形图的Rect
    val barRectList: List<RectF> = BarChartHelper.getBarRectList(barDataList, DEFAULT_EACH_BAR_WIDTH, DEFAULT_TOTAL_BAR_HEIGHT, DEFAULT_SPACING_BETWEEN_TWO_BARS)
    // 视图总高度
    val totalHeight = (DEFAULT_TOTAL_BAR_HEIGHT + DEFAULT_TOTAL_TEXT_HEIGHT).toInt()
    // 柱形图的圆角半径
    val barRadius = DEFAULT_EACH_BAR_WIDTH / 3
    // 已出数据的文本区域背景Rect
    val textBgRect = RectF(0f, DEFAULT_TOTAL_BAR_HEIGHT, totalWidth.toFloat(), DEFAULT_TOTAL_BAR_HEIGHT + DEFAULT_TOTAL_TEXT_HEIGHT)
}