package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData
import com.like.common.view.chart.horizontalScrollBarChartView.entity.getSimulatedData
import com.like.logger.Logger

class BarChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val DEFAULT_COLORS = intArrayOf(// 颜色数组
            0xff02bbff.toInt(),
            0xffa845e7.toInt(),
            0xffed4b90.toInt(),
            0xfff84330.toInt()
    )
    private val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 颜色对应的终点位置的数组
            0.4f, 0.7f, 0.9f, 1.0f
    )
    private val DEFAULT_EACH_BARWIDTH: Float = 40f
    private val DEFAULT_TOTAL_BARHEIGHT: Float = 500f
    private val DEFAULT_SPACING_BETWEEN_TWOBARS: Float = 80f
    private val mBarDataList: List<BarData> = getSimulatedData()
    private val mRectList: List<RectF> = BarChartHelper.getRectData(mBarDataList, DEFAULT_EACH_BARWIDTH, DEFAULT_TOTAL_BARHEIGHT, DEFAULT_SPACING_BETWEEN_TWOBARS)
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.shader = LinearGradient(0f, DEFAULT_TOTAL_BARHEIGHT, 0f, 0f, DEFAULT_COLORS, DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        for (rect in mRectList) {
            Logger.e(rect)
            canvas.drawRect(rect, mPaint)
        }
    }
}