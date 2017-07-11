package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData


class TwoLineChartView(context: Context) : View(context) {
    private val mDataList: MutableList<TwoLineData> = arrayListOf()
    private val mConfig: TwoLineChartConfig = TwoLineChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var currentTouchX = -1f

    init {
        setBackgroundColor(TwoLineChartConfig.DEFAULT_BG_COLOR)

        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = TwoLineChartConfig.DEFAULT_OTHER_LINE_COLOR

        mPathPaint.style = Paint.Style.STROKE
        mPathPaint.strokeWidth = 5f

        mPointPaint.style = Paint.Style.FILL

        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = mConfig.textSize
    }

    fun setData(lineDataList: List<TwoLineData>) {
        mDataList.clear()
        if (lineDataList.isNotEmpty()) {
            mDataList.addAll(lineDataList)
            mConfig.setData(lineDataList)
        }
        currentTouchX = -1f
        requestLayout()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && mConfig.isTouchInView(event.y)) {
            currentTouchX = event.x
            invalidate()
        }
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mConfig.totalWidth.toInt(), mConfig.totalHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        if (mDataList.isNotEmpty()) {
            mDrawHelper = DrawHelper(canvas, mConfig)
            // 画竖直的触摸线
            if (currentTouchX != -1f) {
                mDrawHelper.drawTouchLine(currentTouchX, mLinePaint)
            }
            // 画上中下三条横线
            mDrawHelper.drawTopLine(mLinePaint)
            mDrawHelper.drawBottomLine(mLinePaint)
            mLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
            mDrawHelper.drawMiddleLine(mLinePaint)

            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
            for (index in 0 until mConfig.pointList1.size) {
                mDrawHelper.drawPoint1(index, mPointPaint)// 画环比对应的点圆
            }

            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_0
            for (index in 0 until mConfig.pointList2.size) {
                mDrawHelper.drawPoint2(index, mPointPaint)// 画同比对应的点圆
                mDrawHelper.drawXAxisText(index, mTextPaint)// 画x轴文本
            }

            // 画"0.00%"
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_2
            mDrawHelper.drawMiddleLineText(mTextPaint)

            // 画环比折线路径
            mPathPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
            mDrawHelper.drawPath1(mPathPaint)
            // 画同比折线路径
            mPathPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
            mDrawHelper.drawPath2(mPathPaint)

        }
    }
}