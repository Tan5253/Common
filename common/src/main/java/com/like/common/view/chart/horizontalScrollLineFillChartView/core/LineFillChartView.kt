package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.*
import android.view.View
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

class LineFillChartView(context: Context) : View(context) {
    private val mLineDataList: MutableList<LineData> = arrayListOf()
    private val mLineFillChartConfig: LineFillChartConfig = LineFillChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.WHITE)

        mLinePaint.style = Paint.Style.FILL

        mPointPaint.style = Paint.Style.STROKE
        mPointPaint.color = Color.BLACK
        mPointPaint.strokeWidth = 4f

        mPointFillPaint.style = Paint.Style.FILL
        mPointFillPaint.color = Color.WHITE
    }

    fun setData(lineDataList: List<LineData>) {
        mLineDataList.clear()
        mLineDataList.addAll(lineDataList)

        mLineFillChartConfig.setData(lineDataList)

        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mLineFillChartConfig.totalWidth, mLineFillChartConfig.totalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        if (mLineDataList.isNotEmpty()) {
            mDrawHelper = DrawHelper(canvas, mLineFillChartConfig)
            mLinePaint.shader = LinearGradient(0f, mLineFillChartConfig.totalHeight.toFloat(), 0f, mLineFillChartConfig.linearGradientY1, LineFillChartConfig.DEFAULT_COLORS, LineFillChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)
            // 画折线图，并填充
            for (index in 0 until mLineFillChartConfig.pathList.size) {
                mDrawHelper.drawPath(index, mLinePaint)
            }
            // 画点圆
            for (index in 0 until mLineFillChartConfig.pointList.size) {
                mDrawHelper.drawPoint(index, mPointFillPaint)
                mDrawHelper.drawPoint(index, mPointPaint)
            }
        }
    }
}