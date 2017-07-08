package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.graphics.Canvas
import android.graphics.Paint

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val config: TwoLineChartConfig) {

    inline fun drawTopLine(paint: Paint) = canvas.drawLine(
            0f,
            config.spacingLineViewTop,
            config.totalWidth,
            config.spacingLineViewTop,
            paint
    )

    inline fun drawMiddleLine(paint: Paint) = canvas.drawLine(
            0f,
            config.spacingLineViewTop + config.maxLineViewHeight / 2,
            config.totalWidth,
            config.spacingLineViewTop + config.maxLineViewHeight / 2,
            paint
    )


    inline fun drawBottomLine(paint: Paint) = canvas.drawLine(
            0f,
            config.spacingLineViewTop + config.maxLineViewHeight,
            config.totalWidth,
            config.spacingLineViewTop + config.maxLineViewHeight,
            paint
    )

    inline fun drawPoint1(index: Int, paint: Paint) = canvas.drawCircle(
            config.pointList1[index].x,
            config.pointList1[index].y,
            config.pointCircleRadius,
            paint
    )

    inline fun drawPoint2(index: Int, paint: Paint) = canvas.drawCircle(
            config.pointList2[index].x,
            config.pointList2[index].y,
            config.pointCircleRadius,
            paint
    )
}
