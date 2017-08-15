package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.like.common.util.RxBusTag
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineTouchData
import com.like.rxbus.RxBus


class TwoLineChartView(context: Context) : View(context) {
    private val mDataList: MutableList<TwoLineData> = arrayListOf()
    private val mConfig: TwoLineChartConfig = TwoLineChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var currentTouchX = -1f
    private var touchXData = -Int.MAX_VALUE

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

    fun setData(lineDataList: List<TwoLineData>, touchXData: Int, showPointCount: Int) {
        if (showPointCount <= 0) {
            throw IllegalArgumentException("showPointCount 参数必须大于0")
        }
        mDataList.clear()
        if (lineDataList.isNotEmpty()) {
            mDataList.addAll(lineDataList)
            mConfig.setData(lineDataList, showPointCount)
        }
        this.touchXData = touchXData
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
        mDrawHelper = DrawHelper(canvas, mConfig)
        if (mDataList.isNotEmpty()) {
            // 画竖直的触摸线
            if (currentTouchX != -1f || touchXData != -Int.MAX_VALUE) {
                mDrawHelper.drawTouchLine(currentTouchX, mLinePaint, touchXData)
            }
            // 画上中下三条横线
            mDrawHelper.drawTopLine(mLinePaint)
            mDrawHelper.drawBottomLine(mLinePaint)
            mLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
            mDrawHelper.drawMiddleLine(mLinePaint)

            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_0
            for (index in 0 until mConfig.pointList1.size) {
                mDrawHelper.drawPoint1(index, mPointPaint)// 画点圆
                mDrawHelper.drawXAxisText(index, mTextPaint)// 画x轴文本
            }

            // 画"0.00%"
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_2
            mDrawHelper.drawMiddleLineText(mTextPaint)

            // 画折线路径
            mPathPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
            mDrawHelper.drawPath1(mPathPaint)

            if (mConfig.hasTwoLine()) {
                mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
                for (index in 0 until mConfig.pointList2.size) {
                    mDrawHelper.drawPoint2(index, mPointPaint)// 画点圆
                }
                // 画折线路径
                mPathPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
                mDrawHelper.drawPath2(mPathPaint)
            }

            if (currentTouchX != -1f || touchXData != -Int.MAX_VALUE) {
                // 画触摸线上的值的背景，正数为红色背景，负数为绿色背景
                if (mConfig.touchData1 > 0) {
                    mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_1
                } else if (mConfig.touchData1 < 0) {
                    mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_2
                } else {
                    mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_3
                }
                mDrawHelper.drawTouchPointRect1(mPointPaint)

                // 画触摸线上的值
                mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_1
                mDrawHelper.drawTouchPointText1(mTextPaint)

                if (mConfig.hasTwoLine()) {
                    if (mConfig.touchData2 > 0) {
                        mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_1
                    } else if (mConfig.touchData2 < 0) {
                        mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_2
                    } else {
                        mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_3
                    }
                    mDrawHelper.drawTouchPointRect2(mPointPaint)

                    // 画触摸线上的值
                    mDrawHelper.drawTouchPointText2(mTextPaint)
                }
                if (currentTouchX != -1f)// 触摸时才发送数据
                    RxBus.post(RxBusTag.TAG_TWO_LINE_CHART_VIEW_CLICKED, TwoLineTouchData(mConfig.touchXData, mConfig.touchData1, mConfig.touchData2))
            }
        } else {
            // 画上中下三条横线
            mDrawHelper.drawTopLine(mLinePaint)
            mDrawHelper.drawBottomLine(mLinePaint)
            mLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
            mDrawHelper.drawMiddleLine(mLinePaint)

            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_0
            for (index in 0 until mConfig.pointList1.size) {
                mDrawHelper.drawXAxisText(index, mTextPaint)// 画x轴文本
            }

            // 画"0.00%"
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_2
            mDrawHelper.drawMiddleLineText(mTextPaint)
            mDrawHelper.drawMiddleLineText1(mTextPaint)
        }
    }
}