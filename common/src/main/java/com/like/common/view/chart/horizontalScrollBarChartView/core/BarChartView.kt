package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.*
import android.view.View
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData
import com.like.logger.Logger

class BarChartView(context: Context) : View(context) {
    private val mBarDataList: MutableList<BarData> = arrayListOf()
    private val mBarChartConfig: BarChartConfig = BarChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    private val mBarPaintReal = Paint(Paint.ANTI_ALIAS_FLAG)// 柱形图，真实数据
    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)// 柱形图，预测数据

    private val mMonthTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mElectricityTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mUnitTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mTextBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mOtherTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.WHITE)
    }

    fun setData(barDataList: List<BarData>) {
        mBarDataList.clear()
        mBarDataList.addAll(barDataList)

        mBarChartConfig.setData(barDataList)

        mBarPaintReal.style = Paint.Style.FILL
        mBarPaintReal.shader = LinearGradient(0f, mBarChartConfig.totalBarHeight, 0f, 0f, BarChartConfig.DEFAULT_COLORS_REAL, BarChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)

        mBarPaint.style = Paint.Style.FILL
        mBarPaint.color = BarChartConfig.DEFAULT_COLOR

        mMonthTextPaint.style = Paint.Style.STROKE
        mMonthTextPaint.textSize = mBarChartConfig.monthTextSize

        mElectricityTextPaint.style = Paint.Style.STROKE
        mElectricityTextPaint.textSize = mBarChartConfig.electricityTextSize

        mUnitTextPaint.style = Paint.Style.STROKE
        mUnitTextPaint.textSize = mBarChartConfig.unitTextSize
        mUnitTextPaint.color = BarChartConfig.DEFAULT_UNIT_TEXT_COLOR

        mTextBgPaint.style = Paint.Style.FILL

        mOtherTextPaint.color = BarChartConfig.DEFAULT_OTHER_TEXT_COLOR
        mOtherTextPaint.textSize = mBarChartConfig.otherTextSize
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Logger.wtf("BarChartView onMeasure")
        setMeasuredDimension(mBarChartConfig.totalWidth, mBarChartConfig.totalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        if (mBarDataList.isNotEmpty()) {
            Logger.wtf("BarChartView onDraw")
            mDrawHelper = DrawHelper(canvas, mBarChartConfig)
            // 画单位
            mTextBgPaint.color = BarChartConfig.DEFAULT_UNIT_BG_COLOR
            mDrawHelper.drawUnitBg(mTextBgPaint)
            mDrawHelper.drawUnitText(mUnitTextPaint)

            for ((index, barData) in mBarDataList.withIndex()) {
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