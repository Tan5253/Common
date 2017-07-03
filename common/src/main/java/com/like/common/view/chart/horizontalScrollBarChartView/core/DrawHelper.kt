package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

fun View.drawBar(canvas: Canvas, rect: RectF, roundRectRadius: Float, paint: Paint) {
    canvas.drawRoundRect(rect, roundRectRadius, roundRectRadius, paint)
}

fun View.drawMonth(canvas: Canvas, text: String, barIndex: Int, eachBarWidth: Float, totalBarHeight: Float, spacingBetweenTwoBars: Float, paint: Paint) {
    canvas.drawText(text, barIndex * (eachBarWidth + spacingBetweenTwoBars), totalBarHeight + (paint.fontMetrics.bottom - paint.fontMetrics.top) / 2 + 20f, paint)
}

fun View.drawElectricity(canvas: Canvas, text: String, barIndex: Int, eachBarWidth: Float, totalBarHeight: Float, spacingBetweenTwoBars: Float, paint: Paint) {
    canvas.drawText("${text}度", barIndex * (eachBarWidth + spacingBetweenTwoBars), totalBarHeight + 90, paint)
}

fun View.drawTextBg(canvas: Canvas, rect: RectF, paint: Paint) {
    canvas.drawRect(rect, paint)
}