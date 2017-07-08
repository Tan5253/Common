package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.View
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData


class TwoLineChartView(context: Context) : View(context) {
    private val mDataList: MutableList<TwoLineData> = arrayListOf()
    private val mConfig: TwoLineChartConfig = TwoLineChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(TwoLineChartConfig.DEFAULT_BG_COLOR)

        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = TwoLineChartConfig.DEFAULT_OTHER_LINE_COLOR

        mPointPaint.style = Paint.Style.FILL
    }

    fun setData(lineDataList: List<TwoLineData>) {
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

            mDrawHelper.drawTopLine(mLinePaint)
            mDrawHelper.drawBottomLine(mLinePaint)
            mLinePaint.pathEffect = DashPathEffect(floatArrayOf(3f, 2f), 0f)
            mDrawHelper.drawMiddleLine(mLinePaint)

            for (index in 0 until mConfig.pointList1.size) {
                mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
                mDrawHelper.drawPoint1(index, mPointPaint)
            }
            for (index in 0 until mConfig.pointList2.size) {
                mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
                mDrawHelper.drawPoint2(index, mPointPaint)
            }
        }
    }
}