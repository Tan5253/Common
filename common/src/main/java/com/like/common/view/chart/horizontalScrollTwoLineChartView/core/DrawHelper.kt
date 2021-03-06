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

    inline fun drawTouchLine(touchX: Float, paint: Paint) {
        val touchPointX = config.getCurrentTouchPointX(touchX)
        if (touchPointX != -1f) {
            canvas.drawLine(
                    touchPointX,
                    config.spacingLineViewTop,
                    touchPointX,
                    config.spacingLineViewTop + config.maxLineViewHeight,
                    paint
            )
        }
    }

    inline fun drawPoint1(index: Int, paint: Paint) {
        canvas.drawCircle(
                config.pointList1[index].x,
                config.pointList1[index].y,
                config.pointCircleRadius,
                paint
        )
    }

    inline fun drawHollowPoint1(index: Int, hollowPaint: Paint, hollowFillPaint: Paint) {
        canvas.drawCircle(
                config.pointList1[index].x,
                config.pointList1[index].y,
                config.hollowPointCircleRadius,
                hollowPaint
        )
        canvas.drawCircle(
                config.pointList1[index].x,
                config.pointList1[index].y,
                config.hollowPointCircleRadius - hollowPaint.strokeWidth,
                hollowFillPaint
        )
    }

    inline fun drawPoint2(index: Int, paint: Paint) {
        canvas.drawCircle(
                config.pointList2[index].x,
                config.pointList2[index].y,
                config.pointCircleRadius,
                paint
        )
    }

    inline fun drawHollowPoint2(index: Int, hollowPaint: Paint, hollowFillPaint: Paint) {
        canvas.drawCircle(
                config.pointList2[index].x,
                config.pointList2[index].y,
                config.hollowPointCircleRadius,
                hollowPaint
        )
        canvas.drawCircle(
                config.pointList2[index].x,
                config.pointList2[index].y,
                config.hollowPointCircleRadius - hollowPaint.strokeWidth,
                hollowFillPaint
        )
    }

    inline fun drawXAxisText(index: Int, paint: Paint) {
        val text = config.dataList[index].x.toString()
        canvas.drawText(
                text,
                config.pointList1[index].x - DrawTextUtils.getTextlength(paint, text) / 2,
                config.xAxisStartY,
                paint
        )
    }

    inline fun drawMiddleLineLeftText(paint: Paint) = canvas.drawText(
            "0.00%",
            config.middleLineLeftTextStartX,
            config.middleLineLeftTextStartY,
            paint
    )

    inline fun drawMiddleLineRightText(paint: Paint) = canvas.drawText(
            "0.00%",
            config.middleLineRightTextStartX,
            config.middleLineRightTextStartY,
            paint
    )

    inline fun drawMiddleLineMiddleText(paint: Paint) = canvas.drawText(
            "暂无数据",
            config.middleLineMiddleTextStartX,
            config.middleLineMiddleTextStartY,
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

    inline fun drawTouchPointRect1(paint: Paint) = canvas.drawRoundRect(
            config.touchPointRect1,
            config.touchPointRectRadius,
            config.touchPointRectRadius,
            paint
    )

    inline fun drawTouchPointRect2(paint: Paint) = canvas.drawRoundRect(
            config.touchPointRect2,
            config.touchPointRectRadius,
            config.touchPointRectRadius,
            paint
    )

    inline fun drawTouchPointText1(paint: Paint) {
        if (config.touchPosition != -1) {
            val touchPoint1 = config.pointList1[config.touchPosition]
            val text = config.dataList[config.touchPosition].showData1
            canvas.drawText(
                    text,
                    touchPoint1.x - DrawTextUtils.getTextlength(paint, text) / 2,
                    touchPoint1.y - DrawTextUtils.getTextHeight(paint) / 2 + DrawTextUtils.getTextBaseLine(paint),
                    paint
            )
        }
    }

    inline fun drawTouchPointText2(paint: Paint) {
        if (config.touchPosition != -1) {
            val touchPoint2 = config.pointList2[config.touchPosition]
            val text = config.dataList[config.touchPosition].showData2
            canvas.drawText(
                    text,
                    touchPoint2.x - DrawTextUtils.getTextlength(paint, text) / 2,
                    touchPoint2.y - DrawTextUtils.getTextHeight(paint) / 2 + DrawTextUtils.getTextBaseLine(paint),
                    paint
            )
        }
    }

}
