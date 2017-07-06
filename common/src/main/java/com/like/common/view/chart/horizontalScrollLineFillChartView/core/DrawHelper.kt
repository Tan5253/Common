package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.graphics.Canvas
import android.graphics.Paint

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val lineFillChartConfig: LineFillChartConfig) {

    inline fun drawPath(index: Int, paint: Paint) = canvas.drawPath(lineFillChartConfig.pathList[index], paint)

    inline fun drawPoint(index: Int, paint: Paint) = canvas.drawCircle(lineFillChartConfig.pointList[index].x, lineFillChartConfig.pointList[index].y, lineFillChartConfig.pointCircleRadius, paint)

    inline fun drawGradientBottomRect(paint: Paint) = canvas.drawRect(lineFillChartConfig.gradientBottomRect, paint)
}
