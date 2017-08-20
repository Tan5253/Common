package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.like.common.util.DrawTextUtils
import com.like.common.util.MoneyFormatUtils

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

    inline fun drawPoint1(index: Int, paint: Paint) = canvas.drawCircle(
            config.pointList1[index].x,
            config.pointList1[index].y,
            config.pointCircleRadius,
            paint
    )

    inline fun drawPoint2(index: Int, paint: Paint) {

        canvas.drawCircle(
                config.pointList2[index].x,
                config.pointList2[index].y,
                config.pointCircleRadius,
                paint
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

    inline fun drawMiddleLineText(paint: Paint) = canvas.drawText(
            "0.00%",
            5f,
            config.middleLineTextStartY,
            paint
    )

    inline fun drawMiddleLineText1(paint: Paint) = canvas.drawText(
            "暂无数据",
            config.middleTextStartX1,
            config.middleTextStartY1,
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
            val touchData1 = config.dataList[config.touchPosition].ratio1
            // 这里取绝对值，因为正负靠背景来区分了
            val text = MoneyFormatUtils.formatTwoDecimals(Math.abs(touchData1.toDouble()), MoneyFormatUtils.DECIMAL_TYPE_0_2)
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
            val touchData2 = config.dataList[config.touchPosition].ratio2
            // 这里取绝对值，因为正负靠背景来区分了
            val text = MoneyFormatUtils.formatTwoDecimals(Math.abs(touchData2.toDouble()), MoneyFormatUtils.DECIMAL_TYPE_0_2)
            canvas.drawText(
                    text,
                    touchPoint2.x - DrawTextUtils.getTextlength(paint, text) / 2,
                    touchPoint2.y - DrawTextUtils.getTextHeight(paint) / 2 + DrawTextUtils.getTextBaseLine(paint),
                    paint
            )
        }
    }

}
