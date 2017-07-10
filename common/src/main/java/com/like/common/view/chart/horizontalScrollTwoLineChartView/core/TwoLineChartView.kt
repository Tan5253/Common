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
    private val mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(TwoLineChartConfig.DEFAULT_BG_COLOR)

        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = TwoLineChartConfig.DEFAULT_OTHER_LINE_COLOR

        mPathPaint.style = Paint.Style.STROKE
        mPathPaint.strokeWidth = 5f

        mPointPaint.style = Paint.Style.FILL

        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = mConfig.textSize
        mTextPaint.color = TwoLineChartConfig.DEFAULT_TEXT_COLOR_0
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
            mLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
            mDrawHelper.drawMiddleLine(mLinePaint)

            for (index in 0 until mConfig.pointList1.size) {
                mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
                mDrawHelper.drawPoint1(index, mPointPaint)// 画环比对应的点圆
            }
            for (index in 0 until mConfig.pointList2.size) {
                mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
                mDrawHelper.drawPoint2(index, mPointPaint)// 画同比对应的点圆
                mDrawHelper.drawXAxisText(index, mTextPaint)// 画x轴文本
            }

//            // 画环比图例
//            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_1
//            mDrawHelper.drawHuanBiLegendRect(mPointPaint)
//            // 画同比图例
//            mPointPaint.color = TwoLineChartConfig.DEFAULT_LINE_COLOR_2
//            mDrawHelper.drawTongBiLegendRect(mPointPaint)
//            // 画"单位：%"
//            mDrawHelper.drawUnitText1(mTextPaint)
//            // 画"环比"
//            mDrawHelper.drawHuanBiText(mTextPaint)
//            // 画"同比"
//            mDrawHelper.drawTongBiText(mTextPaint)
//            // 画"单位：日"
//            mDrawHelper.drawUnitText2(mTextPaint)
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