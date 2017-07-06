package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.graphics.Canvas
import android.graphics.Paint

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val lineFillChartConfig: LineFillChartConfig) {

    inline fun drawPath(index: Int, paint: Paint) = canvas.drawPath(lineFillChartConfig.pathList[index], paint)

    inline fun drawPoint(index: Int, paint: Paint) = canvas.drawCircle(lineFillChartConfig.pointList[index].x, lineFillChartConfig.pointList[index].y, lineFillChartConfig.pointCircleRadius, paint)

    inline fun drawGradientBottomRect(paint: Paint) = canvas.drawRect(lineFillChartConfig.gradientBottomRect, paint)

    inline fun drawXAxis(paint: Paint) = canvas.drawLine(0f, lineFillChartConfig.totalGradientAndSpacingTopHeight, lineFillChartConfig.totalWidth, lineFillChartConfig.totalGradientAndSpacingTopHeight, paint)

    inline fun drawXAxisScale(index: Int, paint: Paint) = canvas.drawLine(lineFillChartConfig.pointList[index].x, lineFillChartConfig.totalGradientAndSpacingTopHeight - lineFillChartConfig.xAxisScaleHeight, lineFillChartConfig.pointList[index].x, lineFillChartConfig.totalGradientAndSpacingTopHeight, paint)

}
