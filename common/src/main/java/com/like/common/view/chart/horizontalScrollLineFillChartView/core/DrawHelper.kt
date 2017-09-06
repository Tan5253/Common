package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.graphics.Canvas
import android.graphics.Paint
import com.like.common.util.DrawTextUtils

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val config: LineFillChartConfig) {

    inline fun drawPath(index: Int, paint: Paint) = canvas.drawPath(
            config.pathList[index],
            paint
    )

    inline fun drawPoint(index: Int, paint: Paint) = canvas.drawCircle(
            config.pointList[index].x,
            config.pointList[index].y,
            config.pointCircleRadius,
            paint
    )

    inline fun drawGradientBottomRect(paint: Paint) = canvas.drawRect(
            config.gradientBottomRect,
            paint
    )

    inline fun drawXAxis(paint: Paint) = canvas.drawLine(
            0f,
            config.totalGradientAndSpacingTopHeight,
            config.totalWidth,
            config.totalGradientAndSpacingTopHeight,
            paint
    )

    inline fun drawXAxisScale(index: Int, paint: Paint) = canvas.drawLine(
            config.pointList[index].x,
            config.totalGradientAndSpacingTopHeight - config.xAxisScaleHeight,
            config.pointList[index].x,
            config.totalGradientAndSpacingTopHeight,
            paint
    )

    inline fun drawXAxisText(index: Int, paint: Paint) {
        val text = config.lineDataList[index].xData.toString()
        canvas.drawText(
                text,
                config.pointList[index].x - DrawTextUtils.getTextlength(paint, text) / 2,
                config.xAxisTextStartY,
                paint
        )
    }

    inline fun drawPointText(index: Int, paint: Paint) {
        val text = "${config.lineDataList[index].yData}度"
        canvas.drawText(
                text,
                config.pointList[index].x - DrawTextUtils.getTextlength(paint, text) / 2,
                config.pointList[index].y - DrawTextUtils.getTextHeight(paint) - config.spacingPointTextBottom + DrawTextUtils.getTextBaseLine(paint),
                paint
        )
    }
}
