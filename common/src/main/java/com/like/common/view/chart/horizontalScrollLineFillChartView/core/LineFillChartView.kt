package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.*
import android.view.View
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

class LineFillChartView(context: Context) : View(context) {
    private val mLineDataList: MutableList<LineData> = arrayListOf()
    private val mLineFillChartConfig: LineFillChartConfig = LineFillChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mGradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mGradientBottomPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.WHITE)

        mGradientPaint.style = Paint.Style.FILL

        mPointPaint.style = Paint.Style.STROKE
        mPointPaint.color = LineFillChartConfig.DEFAULT_POINT_BORDER_COLOR
        mPointPaint.strokeWidth = mLineFillChartConfig.pointBorderWidth

        mPointFillPaint.style = Paint.Style.FILL
        mPointFillPaint.color = LineFillChartConfig.DEFAULT_POINT_FILL_COLOR

        mGradientBottomPaint.style = Paint.Style.FILL
        mGradientBottomPaint.color = LineFillChartConfig.DEFAULT_GRADIENT_BOTTOM_BG_COLOR
    }

    fun setData(lineDataList: List<LineData>) {
        mLineDataList.clear()
        mLineDataList.addAll(lineDataList)

        mLineFillChartConfig.setData(lineDataList)

        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mLineFillChartConfig.totalWidth.toInt(), mLineFillChartConfig.totalHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        if (mLineDataList.isNotEmpty()) {
            mDrawHelper = DrawHelper(canvas, mLineFillChartConfig)
            mGradientPaint.shader = LinearGradient(0f, mLineFillChartConfig.totalGradientAndSpacingTopHeight.toFloat(), 0f, mLineFillChartConfig.linearGradientY1, LineFillChartConfig.DEFAULT_COLORS, LineFillChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)
            // 画折线图，并填充
            for (index in 0 until mLineFillChartConfig.pathList.size) {
                mDrawHelper.drawPath(index, mGradientPaint)
            }
            // 画点圆
            for (index in 0 until mLineFillChartConfig.pointList.size) {
                mDrawHelper.drawPoint(index, mPointFillPaint)
                mDrawHelper.drawPoint(index, mPointPaint)
            }

            mDrawHelper.drawGradientBottomRect(mGradientBottomPaint)
        }
    }
}