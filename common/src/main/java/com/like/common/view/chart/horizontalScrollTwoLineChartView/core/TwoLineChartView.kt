package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.View
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData
import com.like.logger.Logger


class TwoLineChartView(context: Context) : View(context) {
    private val mDataList: MutableList<TwoLineData> = arrayListOf()
    private val mConfig: TwoLineChartConfig = TwoLineChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(TwoLineChartConfig.DEFAULT_BG_COLOR)

        mLinePaint.style = Paint.Style.STROKE
//        mLinePaint.color = TwoLineChartConfig.DEFAULT_OTHER_LINE_COLOR
        mLinePaint.color = Color.RED
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

            Logger.wtf("width=${mConfig.totalWidth} height=${mConfig.totalHeight}")

            canvas.drawLine(0f, mConfig.spacingLineViewTop, mConfig.totalWidth, mConfig.spacingLineViewTop, mLinePaint)
            canvas.drawLine(0f, mConfig.spacingLineViewTop + mConfig.maxLineViewHeight, mConfig.totalWidth, mConfig.spacingLineViewTop + mConfig.maxLineViewHeight, mLinePaint)
            mLinePaint.pathEffect = DashPathEffect(floatArrayOf(3f, 2f), 0f)
            canvas.drawLine(0f, mConfig.spacingLineViewTop + mConfig.maxLineViewHeight / 2, mConfig.totalWidth, mConfig.spacingLineViewTop + mConfig.maxLineViewHeight / 2, mLinePaint)
        }
    }
}