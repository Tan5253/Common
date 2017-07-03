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

    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mMonthTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mElectricityTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mTextBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.BLACK)
        mBarPaint.style = Paint.Style.FILL
        mBarPaint.shader = LinearGradient(0f, BarChartConfig.DEFAULT_TOTAL_BAR_HEIGHT, 0f, 0f, BarChartConfig.DEFAULT_COLORS, BarChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)

        mMonthTextPaint.style = Paint.Style.STROKE
        mMonthTextPaint.textSize = mBarChartConfig.monthTextSize
        mMonthTextPaint.color = BarChartConfig.DEFAULT_MONTH_TEXT_COLOR

        mElectricityTextPaint.style = Paint.Style.STROKE
        mElectricityTextPaint.textSize = mBarChartConfig.electricityTextSize
        mElectricityTextPaint.color = BarChartConfig.DEFAULT_ELECTRICITY_TEXT_COLOR

        mTextBgPaint.style = Paint.Style.FILL
        mTextBgPaint.color = BarChartConfig.DEFAULT_TEXT_BG_COLOR
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mBarChartConfig.totalWidth, mBarChartConfig.totalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        mDrawHelper = DrawHelper(canvas, mBarChartConfig)
        mDrawHelper.drawTextBg(mTextBgPaint)
        for (index in 0 until mBarDataList.size) {
            mDrawHelper.drawBar(index, mBarPaint)
            mDrawHelper.drawMonth(index, mMonthTextPaint)
            mDrawHelper.drawElectricity(index, mElectricityTextPaint)
        }
    }
}