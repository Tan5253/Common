package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.view.View
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

class LineFillChartView(context: Context) : View(context) {
    private val mDataList: MutableList<LineData> = arrayListOf()
    private val mConfig: LineFillChartConfig = LineFillChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mGradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mGradientBottomPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mXAxisPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(LineFillChartConfig.DEFAULT_BG_COLOR)

        mGradientPaint.style = Paint.Style.FILL

        mPointPaint.style = Paint.Style.STROKE
        mPointPaint.color = LineFillChartConfig.DEFAULT_POINT_BORDER_COLOR
        mPointPaint.strokeWidth = mConfig.pointBorderWidth

        mPointFillPaint.style = Paint.Style.FILL
        mPointFillPaint.color = LineFillChartConfig.DEFAULT_POINT_FILL_COLOR

        mGradientBottomPaint.style = Paint.Style.FILL
        mGradientBottomPaint.color = LineFillChartConfig.DEFAULT_GRADIENT_BOTTOM_BG_COLOR

        mXAxisPaint.style = Paint.Style.STROKE
        mXAxisPaint.color = LineFillChartConfig.DEFAULT_X_AXIS_BORDER_COLOR
    }

    fun setData(lineDataList: List<LineData>) {
        mDataList.clear()
        if (lineDataList.isNotEmpty()) {
            mDataList.addAll(lineDataList)
            mConfig.setData(lineDataList)
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mConfig.totalWidth.toInt(), mConfig.totalHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        if (mDataList.isNotEmpty()) {
            mDrawHelper = DrawHelper(canvas, mConfig)

            mGradientPaint.shader = LinearGradient(0f, mConfig.totalGradientAndSpacingTopHeight, 0f, mConfig.linearGradientY1, LineFillChartConfig.DEFAULT_COLORS, LineFillChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)

            // 画折线图，并填充
            for (index in 0 until mConfig.pathList.size) {
                mDrawHelper.drawPath(index, mGradientPaint)
            }

            // 画渐变色块以下间隔的背景
            mDrawHelper.drawGradientBottomRect(mGradientBottomPaint)

            // 画x轴线
            mDrawHelper.drawXAxis(mXAxisPaint)

            mXAxisPaint.color = LineFillChartConfig.DEFAULT_X_AXIS_SCALE_COLOR
            for (index in 0 until mConfig.pointList.size) {
                // 画点圆
                mDrawHelper.drawPoint(index, mPointFillPaint)
                mDrawHelper.drawPoint(index, mPointPaint)
                // 画x轴刻度线
                mDrawHelper.drawXAxisScale(index, mXAxisPaint)
                // 画x轴文本
                mTextPaint.textSize = mConfig.xAxisTextSize
                mTextPaint.color = LineFillChartConfig.DEFAULT_X_AXIS_TEXT_COLOR
                mDrawHelper.drawXAxisText(index, mTextPaint)
                // 画点的数值
                mTextPaint.textSize = mConfig.pointTextSize
                mTextPaint.color = LineFillChartConfig.DEFAULT_POINT_TEXT_COLOR
                mDrawHelper.drawPointText(index, mTextPaint)
            }

            // 画x轴下面的单位文本
            mTextPaint.textSize = mConfig.unitTextSize
            mTextPaint.color = LineFillChartConfig.DEFAULT_UNIT_TEXT_COLOR
            mDrawHelper.drawUnitText(mTextPaint)
        }
    }
}