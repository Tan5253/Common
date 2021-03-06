package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.view.View
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

class BarChartView(context: Context) : View(context) {
    private val mDataList: MutableList<BarData> = arrayListOf()
    private val mConfig: BarChartConfig = BarChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mBarPaintReal = Paint(Paint.ANTI_ALIAS_FLAG)// 柱形图，真实数据
    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)// 柱形图，预测数据

    private val mMonthTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mElectricityTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mUnitTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mTextBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mOtherTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(BarChartConfig.DEFAULT_BG_COLOR)

        mBarPaintReal.style = Paint.Style.FILL

        mBarPaint.style = Paint.Style.FILL
        mBarPaint.color = BarChartConfig.DEFAULT_COLOR

        mMonthTextPaint.style = Paint.Style.STROKE
        mMonthTextPaint.textSize = mConfig.monthTextSize

        mElectricityTextPaint.style = Paint.Style.STROKE
        mElectricityTextPaint.textSize = mConfig.electricityTextSize

        mUnitTextPaint.style = Paint.Style.STROKE
        mUnitTextPaint.textSize = mConfig.unitTextSize
        mUnitTextPaint.color = BarChartConfig.DEFAULT_UNIT_TEXT_COLOR

        mTextBgPaint.style = Paint.Style.FILL

        mOtherTextPaint.color = BarChartConfig.DEFAULT_OTHER_TEXT_COLOR
        mOtherTextPaint.textSize = mConfig.otherTextSize
    }

    fun setData(barDataList: List<BarData>) {
        mDataList.clear()
        if (barDataList.isNotEmpty()) {
            mDataList.addAll(barDataList)
            mConfig.setData(barDataList)
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mConfig.totalWidth, mConfig.totalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        if (mDataList.isNotEmpty()) {
            mDrawHelper = DrawHelper(canvas, mConfig)

            mBarPaintReal.shader = LinearGradient(0f, mConfig.maxBarHeight + mConfig.spacingBarTop, 0f, mConfig.linearGradientY1, BarChartConfig.DEFAULT_COLORS_REAL, BarChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)

            // 画单位
            mTextBgPaint.color = BarChartConfig.DEFAULT_UNIT_BG_COLOR
            mDrawHelper.drawUnitBg(mTextBgPaint)
            mDrawHelper.drawUnitText(mUnitTextPaint)

            for ((index, barData) in mDataList.withIndex()) {
                if (barData.isRealData) {
                    mDrawHelper.drawBar(index, mBarPaintReal)
                    mTextBgPaint.color = BarChartConfig.DEFAULT_TEXT_AREA_BG_COLOR_REAL
                    mMonthTextPaint.color = BarChartConfig.DEFAULT_MONTH_TEXT_COLOR_REAL
                    mElectricityTextPaint.color = BarChartConfig.DEFAULT_ELECTRICITY_TEXT_COLOR_REAL
                } else {
                    mDrawHelper.drawBar(index, mBarPaint)
                    mDrawHelper.drawOtherText(index, mOtherTextPaint)
                    mTextBgPaint.color = BarChartConfig.DEFAULT_TEXT_AREA_BG_COLOR
                    mMonthTextPaint.color = BarChartConfig.DEFAULT_MONTH_TEXT_COLOR
                    mElectricityTextPaint.color = BarChartConfig.DEFAULT_ELECTRICITY_TEXT_COLOR
                }
                mDrawHelper.drawXAxisTextBg(index, mTextBgPaint)
                mDrawHelper.drawMonth(index, mMonthTextPaint)
                mDrawHelper.drawElectricity(index, mElectricityTextPaint)
            }
        }
    }
}