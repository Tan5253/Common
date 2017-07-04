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

    inline fun drawOtherText(barIndex: Int, paint: Paint) = canvas.drawText(
            "(预测)",
            barIndex * barAndSpacingWidth + BarChartConfig.DEFAULT_SPACING_BETWEEN_TWO_BARS / 2 - 30,
            barChartConfig.barRectList[barIndex].top - 10,
            paint
    )

    inline fun drawMonth(barIndex: Int, paint: Paint) {
        val text = barChartConfig.barDataList[barIndex].month.toString()
        canvas.drawText(
                text,
                barIndex * barAndSpacingWidth + barAndSpacingWidth / 2 - barChartConfig.getFontlength(paint, text) / 2,
                barChartConfig.monthTextStartY,
                paint
        )
    }

    inline fun drawElectricity(barIndex: Int, paint: Paint) {
        val text = barChartConfig.barDataList[barIndex].electricity.toString()
        canvas.drawText(
                text,
                barIndex * barAndSpacingWidth + barAndSpacingWidth / 2 - barChartConfig.getFontlength(paint, text) / 2,
                barChartConfig.electricityTextStartY,
                paint
        )
    }

    inline fun drawXAxisTextBg(barIndex: Int, paint: Paint) {
        barChartConfig.textBgRect.left = barIndex * barAndSpacingWidth
        barChartConfig.textBgRect.right = barChartConfig.textBgRect.left + barAndSpacingWidth + BarChartConfig.DEFAULT_SPACING_BETWEEN_TWO_BARS / 2
        canvas.drawRect(barChartConfig.textBgRect, paint)
    }

}
