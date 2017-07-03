package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.*
import android.view.View
import com.like.common.util.DimensionUtils
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData
import com.like.common.view.chart.horizontalScrollBarChartView.entity.getSimulatedData

class BarChartView(context: Context) : View(context) {
    private val DEFAULT_TEXT_BG_COLOR = 0xfff0000.toInt()
    private val DEFAULT_MONTH_TEXT_COLOR = 0xffffffff.toInt()
    private val DEFAULT_ELECTRICITY_TEXT_COLOR = 0xffffff00.toInt()
    private val DEFAULT_COLORS = intArrayOf(// 颜色数组
            0xff02bbff.toInt(),
            0xffa845e7.toInt(),
            0xffed4b90.toInt(),
            0xfff84330.toInt()
    )
    private val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 颜色对应的终点位置的数组
            0.4f, 0.7f, 0.9f, 1.0f
    )
    private val DEFAULT_EACH_BARWIDTH: Float = 35f
    private val DEFAULT_TOTAL_BARHEIGHT: Float = 500f
    private val DEFAULT_TOTAL_TEXT_HEIGHT: Int = 110
    private val DEFAULT_SPACING_BETWEEN_TWOBARS: Float = 100f
    private val mBarDataList: List<BarData> = getSimulatedData()
    private val mRectList: List<RectF> = BarChartHelper.getRectData(mBarDataList, DEFAULT_EACH_BARWIDTH, DEFAULT_TOTAL_BARHEIGHT, DEFAULT_SPACING_BETWEEN_TWOBARS)
    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mMonthTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mMonthTextSize = DimensionUtils.sp2px(context, 12f).toFloat()
    private val mElectricityTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mElectricityTextSize = DimensionUtils.sp2px(context, 16f).toFloat()
    private val mTextBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTotalWidth = DEFAULT_EACH_BARWIDTH * mBarDataList.size + DEFAULT_SPACING_BETWEEN_TWOBARS * (mBarDataList.size - 1)
    private val mRoundRectRadius = DEFAULT_EACH_BARWIDTH / 3

    init {
        setBackgroundColor(Color.BLACK)
        mBarPaint.style = Paint.Style.FILL
        mBarPaint.shader = LinearGradient(0f, DEFAULT_TOTAL_BARHEIGHT, 0f, 0f, DEFAULT_COLORS, DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)

        mMonthTextPaint.style = Paint.Style.STROKE
        mMonthTextPaint.textSize = mMonthTextSize
        mMonthTextPaint.color = DEFAULT_MONTH_TEXT_COLOR

        mElectricityTextPaint.style = Paint.Style.STROKE
        mElectricityTextPaint.textSize = mElectricityTextSize
        mElectricityTextPaint.color = DEFAULT_ELECTRICITY_TEXT_COLOR

        mTextBgPaint.style = Paint.Style.FILL
        mTextBgPaint.color = DEFAULT_TEXT_BG_COLOR
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mTotalWidth.toInt(), DEFAULT_TOTAL_BARHEIGHT.toInt() + DEFAULT_TOTAL_TEXT_HEIGHT)
    }

    override fun onDraw(canvas: Canvas) {
        for ((index, value) in mBarDataList.withIndex()) {
            drawBar(canvas, mRectList[index], mRoundRectRadius, mBarPaint)
            drawMonth(canvas, value.month.toString(), index, DEFAULT_EACH_BARWIDTH, DEFAULT_TOTAL_BARHEIGHT, DEFAULT_SPACING_BETWEEN_TWOBARS, mMonthTextPaint)
            drawElectricity(canvas, value.electricity.toString(), index, DEFAULT_EACH_BARWIDTH, DEFAULT_TOTAL_BARHEIGHT, DEFAULT_SPACING_BETWEEN_TWOBARS, mElectricityTextPaint)
        }
    }
}