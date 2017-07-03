package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.Canvas
import android.graphics.Paint

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val barChartConfig: BarChartConfig) {
    // 1个柱形图+1个间隔的总宽度
    val barAndSpacingWidth = BarChartConfig.DEFAULT_EACH_BAR_WIDTH + BarChartConfig.DEFAULT_SPACING_BETWEEN_TWO_BARS

    inline fun drawBar(barIndex: Int, paint: Paint) = canvas.drawRoundRect(
            barChartConfig.barRectList[barIndex],
            barChartConfig.barRadius,
            barChartConfig.barRadius,
            paint
    )

    inline fun drawMonth(barIndex: Int, paint: Paint) = canvas.drawText(
            barChartConfig.barDataList[barIndex].month.toString(),
            barIndex * barAndSpacingWidth,
            barChartConfig.monthTextStartY,
            paint
    )

    inline fun drawElectricity(barIndex: Int, paint: Paint) = canvas.drawText(
            "${barChartConfig.barDataList[barIndex].electricity}度",
            barIndex * barAndSpacingWidth,
            barChartConfig.electricityTextStartY,
            paint
    )

    inline fun drawTextBg(paint: Paint) = canvas.drawRect(
            barChartConfig.textBgRect,
            paint
    )

}
