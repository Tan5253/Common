package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.*
import android.view.View
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData
import com.like.common.view.chart.horizontalScrollBarChartView.entity.getSimulatedData

class BarChartView(context: Context) : View(context) {
    private val mBarDataList: List<BarData> = getSimulatedData()
    private val mBarChartConfig: BarChartConfig = BarChartConfig(context, mBarDataList)
    private lateinit var mDrawHelper: DrawHelper

    private val mBarPaintReal = Paint(Paint.ANTI_ALIAS_FLAG)// 柱形图，真实数据
    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)// 柱形图，预测数据

    private val mMonthTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mElectricityTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mTextBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mOtherTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.WHITE)
        mBarPaintReal.style = Paint.Style.FILL
        mBarPaintReal.shader = LinearGradient(0f, mBarChartConfig.totalBarHeight, 0f, 0f, BarChartConfig.DEFAULT_COLORS_REAL, BarChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)

        mBarPaint.style = Paint.Style.FILL
        mBarPaint.color = BarChartConfig.DEFAULT_COLOR

        mMonthTextPaint.style = Paint.Style.STROKE
        mMonthTextPaint.textSize = mBarChartConfig.monthTextSize

        mElectricityTextPaint.style = Paint.Style.STROKE
        mElectricityTextPaint.textSize = mBarChartConfig.electricityTextSize

        mTextBgPaint.style = Paint.Style.FILL

        mOtherTextPaint.color = BarChartConfig.DEFAULT_OTHER_TEXT_COLOR
        mOtherTextPaint.textSize = mBarChartConfig.otherTextSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mBarChartConfig.totalWidth, mBarChartConfig.totalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        mDrawHelper = DrawHelper(canvas, mBarChartConfig)
        for ((index, barData) in mBarDataList.withIndex()) {
            if (barData.isRealData) {
                mDrawHelper.drawBar(index, mBarPaintReal)
                mTextBgPaint.color = BarChartConfig.DEFAULT_TEXT_BG_COLOR_REAL
                mMonthTextPaint.color = BarChartConfig.DEFAULT_MONTH_TEXT_COLOR_REAL
                mElectricityTextPaint.color = BarChartConfig.DEFAULT_ELECTRICITY_TEXT_COLOR_REAL
            } else {
                mDrawHelper.drawBar(index, mBarPaint)
                mDrawHelper.drawOtherText(index, mOtherTextPaint)
                mTextBgPaint.color = BarChartConfig.DEFAULT_TEXT_BG_COLOR
                mMonthTextPaint.color = BarChartConfig.DEFAULT_MONTH_TEXT_COLOR
                mElectricityTextPaint.color = BarChartConfig.DEFAULT_ELECTRICITY_TEXT_COLOR
            }
            mDrawHelper.drawXAxisTextBg(index, mTextBgPaint)
            mDrawHelper.drawMonth(index, mMonthTextPaint)
            mDrawHelper.drawElectricity(index, mElectricityTextPaint)
        }
    }
}