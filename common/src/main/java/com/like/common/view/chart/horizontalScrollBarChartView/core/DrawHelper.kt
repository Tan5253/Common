package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.Canvas
import android.graphics.Paint

class DrawHelper(val canvas: Canvas, val barChartConfig: BarChartConfig) {
    val barAndSpacingWidth = BarChartConfig.DEFAULT_EACH_BAR_WIDTH + BarChartConfig.DEFAULT_SPACING_BETWEEN_TWO_BARS
    val monthTextTop = BarChartConfig.DEFAULT_TOTAL_BAR_HEIGHT + BarChartConfig.DEFAULT_SPACING_ON_TEXT_TOP_OR_BOTTOM
    var electricityTextTop = 0f
    var monthTextHeight = 0f

    fun drawBar(barIndex: Int, paint: Paint) {
        canvas.drawRoundRect(
                barChartConfig.barRectList[barIndex],
                barChartConfig.barRadius,
                barChartConfig.barRadius,
                paint
        )
    }

    fun drawMonth(barIndex: Int, paint: Paint) {
        if (monthTextHeight == 0f) {
            monthTextHeight = Math.abs(paint.fontMetrics.ascent) + Math.abs(paint.fontMetrics.descent)
        }
        if (electricityTextTop == 0f) {
            electricityTextTop = monthTextTop + monthTextHeight
        }
        canvas.drawText(
                barChartConfig.barDataList[barIndex].month.toString(),
                barIndex * barAndSpacingWidth,
                monthTextTop + (paint.fontMetrics.bottom - paint.fontMetrics.top) / 2,
                paint
        )
    }

    fun drawElectricity(barIndex: Int, paint: Paint) {
        canvas.drawText(
                "${barChartConfig.barDataList[barIndex].electricity}度",
                barIndex * barAndSpacingWidth,
                electricityTextTop,
                paint
        )
    }

    fun drawTextBg(paint: Paint) {
        canvas.drawRect(
                barChartConfig.textBgRect,
                paint
        )
    }
}
