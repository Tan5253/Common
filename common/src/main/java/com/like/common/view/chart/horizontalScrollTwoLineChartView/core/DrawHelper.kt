package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.like.common.util.DrawTextUtils

@Suppress("NOTHING_TO_INLINE")
class DrawHelper(val canvas: Canvas, val config: TwoLineChartConfig) {

    inline fun drawTopLine(paint: Paint) = canvas.drawLine(
            0f,
            config.spacingLineViewTop,
            config.totalWidth,
            config.spacingLineViewTop,
            paint
    )

    inline fun drawMiddleLine(paint: Paint) {
        val path = Path()
        path.moveTo(0f, config.spacingLineViewTop + config.maxLineViewHeight / 2)
        path.lineTo(config.totalWidth, config.spacingLineViewTop + config.maxLineViewHeight / 2)
        canvas.drawPath(path, paint)
    }


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

    inline fun drawXAxisText(index: Int, paint: Paint) {
        val text = config.dataList[index].x.toString()
        canvas.drawText(
                text,
                config.pointList1[index].x - DrawTextUtils.getTextlength(paint, text) / 2,
                config.xAxisStartY,
                paint
        )
    }

    inline fun drawUnitText1(paint: Paint) = canvas.drawText(
            "单位：%",
            config.spacingUnitTextLeft,
            config.unitText1StartY,
            paint
    )

    inline fun drawUnitText2(paint: Paint) = canvas.drawText(
            "单位：日",
            config.spacingUnitTextLeft,
            config.unitText2StartY,
            paint
    )

    inline fun drawMiddleLineText(paint: Paint) = canvas.drawText(
            "0.00%",
            5f,
            config.middleLineTextStartY,
            paint
    )

    inline fun drawHuanBiText(paint: Paint) = canvas.drawText(
            "环比",
            config.legendRect1.right + config.spacingBetweenLegendAndText1,
            config.unitText1StartY,
            paint
    )

    inline fun drawTongBiText(paint: Paint) = canvas.drawText(
            "同比",
            config.legendRect2.right + config.spacingBetweenLegendAndText1,
            config.unitText1StartY,
            paint
    )

    inline fun drawHuanBiLegendRect(paint: Paint) = canvas.drawRect(
            config.legendRect1,
            paint
    )

    inline fun drawTongBiLegendRect(paint: Paint) = canvas.drawRect(
            config.legendRect2,
            paint
    )

    inline fun drawPath1(paint: Paint) = canvas.drawPath(
            config.path1,
            paint
    )

    inline fun drawPath2(paint: Paint) = canvas.drawPath(
            config.path2,
            paint
    )
}
