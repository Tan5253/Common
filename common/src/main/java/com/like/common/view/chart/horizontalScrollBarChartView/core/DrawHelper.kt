package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.Canvas
import android.graphics.Paint
import com.like.common.util.DrawTextUtils

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val config: BarChartConfig) {
    // 1个柱形图+1个间隔的总宽度
    val barAndSpacingWidth = config.eachBarWidth + config.spacingBetweenTwoBars

    inline fun drawBar(barIndex: Int, paint: Paint) = canvas.drawRoundRect(
            config.barRectList[barIndex],
            config.barRadius,
            config.barRadius,
            paint
    )

    inline fun drawOtherText(barIndex: Int, paint: Paint) {
        val text = "(预测)"
        canvas.drawText(
                text,
                config.spacingBetweenTwoBars + barIndex * barAndSpacingWidth + config.spacingBetweenTwoBars / 2 + config.eachBarWidth / 2 - paint.measureText(text) / 2,
                config.barRectList[barIndex].top - DrawTextUtils.getTextHeight(paint) + DrawTextUtils.getTextBaseLine(paint),
                paint
        )
    }

    inline fun drawMonth(barIndex: Int, paint: Paint) {
        val text = config.barDataList[barIndex].month.toString()
        canvas.drawText(
                text,
                config.spacingBetweenTwoBars + barIndex * barAndSpacingWidth + barAndSpacingWidth / 2 - DrawTextUtils.getTextlength(paint, text) / 2,
                config.monthTextStartY,
                paint
        )
    }

    inline fun drawElectricity(barIndex: Int, paint: Paint) {
        val text = config.barDataList[barIndex].electricity.toString()
        canvas.drawText(
                text,
                config.spacingBetweenTwoBars + barIndex * barAndSpacingWidth + barAndSpacingWidth / 2 - DrawTextUtils.getTextlength(paint, text) / 2,
                config.electricityTextStartY,
                paint
        )
    }

    inline fun drawXAxisTextBg(barIndex: Int, paint: Paint) {
        canvas.drawRect(
                config.spacingBetweenTwoBars + barIndex * barAndSpacingWidth,
                config.textAreaBgRect.top,
                config.spacingBetweenTwoBars + barIndex * barAndSpacingWidth + barAndSpacingWidth + config.spacingBetweenTwoBars / 2,
                config.textAreaBgRect.bottom,
                paint
        )
    }

    inline fun drawUnitText(paint: Paint) {
        canvas.drawText("日", (config.spacingBetweenTwoBars - paint.measureText("日")) / 2, config.monthTextStartY, paint)
        canvas.drawText("度", (config.spacingBetweenTwoBars - paint.measureText("度")) / 2, config.electricityTextStartY, paint)
    }

    inline fun drawUnitBg(paint: Paint) {
        canvas.drawRect(
                0f,
                config.textAreaBgRect.top,
                config.spacingBetweenTwoBars,
                config.textAreaBgRect.bottom,
                paint
        )
    }
}
