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
    val mConfig: TwoLineChartConfig = TwoLineChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var currentTouchX = -1f

    private var preTouchPosition = -1
    private var listener: com.like.common.view.chart.horizontalScrollTwoLineChartView.core.OnClickListener? = null

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

    fun setData(twoLineDataList: List<TwoLineData>, touchPosition: Int, showPointCount: Int, listener: com.like.common.view.chart.horizontalScrollTwoLineChartView.core.OnClickListener? = null) {
        this.listener = listener
        mDataList.clear()
        if (twoLineDataList.isNotEmpty()) {
            mDataList.addAll(twoLineDataList)
            mConfig.setData(twoLineDataList, touchPosition, showPointCount)
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
        mDrawHelper = DrawHelper(canvas, mConfig)
        draw3HorizontalLine()// 画上中下三条横线
        drawXAxisText()// 画x轴文本
        drawPercent0Text()// 画"0.00%"
        if (mDataList.isNotEmpty()) {
            drawPath1()// 画折线路径1
            drawPath2()// 画折线路径2
            drawTouchLine()// 画竖直的触摸线及其数值
        } else {
            drawNoDataText()// 画"暂无数据"
        }
    }

    /**
     * 画竖直的触摸线及触摸线上的值
     */
    private fun drawTouchLine() {
        if (!mConfig.hasLine1() && !mConfig.hasLine2()) {
            return
        }
        if (currentTouchX == -1f && mConfig.touchPosition == -1) {
            return
        }
        // 画竖直的触摸线
        mDrawHelper.drawTouchLine(currentTouchX, mLinePaint)

        // 画折线1的值及其背景
        if (mConfig.hasLine1()) {
            // 画触摸线上的值的背景，正数为红色背景，负数为绿色背景
            val touchData1 = mDataList[mConfig.touchPosition].ratio1
            when {
                touchData1 > 0 -> mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_1
                touchData1 < 0 -> mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_2
                else -> mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_3
            }
            mDrawHelper.drawTouchPointRect1(mPointPaint)
            // 画触摸线上的值
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_1
            mDrawHelper.drawTouchPointText1(mTextPaint)
        }
        // 画折线2的值及其背景
        if (mConfig.hasLine2()) {
            // 画触摸线上的值的背景，正数为红色背景，负数为绿色背景
            val touchData2 = mDataList[mConfig.touchPosition].ratio2
            when {
                touchData2 > 0 -> mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_1
                touchData2 < 0 -> mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_2
                else -> mPointPaint.color = TwoLineChartConfig.DEFAULT_TEXT_BG_COLOR_3
            }
            mDrawHelper.drawTouchPointRect2(mPointPaint)

            // 画触摸线上的值
            mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_1
            mDrawHelper.drawTouchPointText2(mTextPaint)
        }
        // 发送当前触摸点的位置
        if (mConfig.touchPosition != preTouchPosition) {
            listener?.onClick(mConfig.touchPosition)
            preTouchPosition = mConfig.touchPosition
        }
    }

    /**
     *  画上中下三条横线
     */
    private fun draw3HorizontalLine() {
        mDrawHelper.drawTopLine(mLinePaint)
        mDrawHelper.drawBottomLine(mLinePaint)
        mLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
        mDrawHelper.drawMiddleLine(mLinePaint)
    }

    /**
     * 画x轴文本
     */
    private fun drawXAxisText() {
        mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_0
        for (index in 0 until mConfig.pointList1.size) {
            mDrawHelper.drawXAxisText(index, mTextPaint)
        }
    }

    /**
     * 画"0.00%"
     */
    private fun drawPercent0Text() {
        mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_2
        mDrawHelper.drawMiddleLineText(mTextPaint)
    }

    /**
     * 画"暂无数据"
     */
    private fun drawNoDataText() {
        mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_2
        mDrawHelper.drawMiddleLineText1(mTextPaint)
    }

    /**
     * 画折线路径1及其点圆
     */
    private fun drawPath1() {
        if (mConfig.hasLine1()) {
            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
            for (index in 0 until mConfig.pointList1.size) {
                mDrawHelper.drawPoint1(index, mPointPaint)
            }
            mPathPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
            mDrawHelper.drawPath1(mPathPaint)
        }
    }

    /**
     * 画折线路径2及其点圆
     */
    private fun drawPath2() {
        if (mConfig.hasLine2()) {
            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
            for (index in 0 until mConfig.pointList2.size) {
                mDrawHelper.drawPoint2(index, mPointPaint)// 画点圆
            }
            // 画折线路径
            mPathPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
            mDrawHelper.drawPath2(mPathPaint)
        }
    }

}