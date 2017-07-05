package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.Canvas
import android.graphics.Paint

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val barChartConfig: BarChartConfig) {
    // 1个柱形图+1个间隔的总宽度
    val barAndSpacingWidth = barChartConfig.eachBarWidth + barChartConfig.spacingBetweenTwoBars

    inline fun drawBar(barIndex: Int, paint: Paint) = canvas.drawRoundRect(
            barChartConfig.barRectList[barIndex],
            barChartConfig.barRadius,
            barChartConfig.barRadius,
            paint
    )

    inline fun drawOtherText(barIndex: Int, paint: Paint) {
        val text = "(预测)"
        canvas.drawText(
                text,
                barIndex * barAndSpacingWidth + barChartConfig.spacingBetweenTwoBars / 2 + barChartConfig.eachBarWidth / 2 - paint.measureText(text) / 2,
                barChartConfig.barRectList[barIndex].top - barChartConfig.getTextHeight(paint) + barChartConfig.getTextBaseLine(paint),
                paint
        )
    }

    inline fun drawMonth(barIndex: Int, paint: Paint) {
        val text = barChartConfig.barDataList[barIndex].month.toString()
        canvas.drawText(
                text,
                barIndex * barAndSpacingWidth + barAndSpacingWidth / 2 - barChartConfig.getTextlength(paint, text) / 2,
                barChartConfig.monthTextStartY,
                paint
        )
    }

    inline fun drawElectricity(barIndex: Int, paint: Paint) {
        val text = barChartConfig.barDataList[barIndex].electricity.toString()
        canvas.drawText(
                text,
                barIndex * barAndSpacingWidth + barAndSpacingWidth / 2 - barChartConfig.getTextlength(paint, text) / 2,
                barChartConfig.electricityTextStartY,
                paint
        )
    }

    inline fun drawXAxisTextBg(barIndex: Int, paint: Paint) {
        canvas.drawRect(
                barIndex * barAndSpacingWidth,
                barChartConfig.textAreaBgRect.top,
                barIndex * barAndSpacingWidth + barAndSpacingWidth + barChartConfig.spacingBetweenTwoBars / 2,
                barChartConfig.textAreaBgRect.bottom,
                paint
        )
    }

}
