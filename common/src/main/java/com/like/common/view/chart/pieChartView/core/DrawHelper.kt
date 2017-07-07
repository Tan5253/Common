package com.like.common.view.chart.pieChartView.core

import android.graphics.Canvas
import android.graphics.Paint

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val config: PieChartConfig) {

    inline fun drawSector(index: Int, paint: Paint) = canvas.drawPath(
            config.pathList[index],
            paint
    )

}
