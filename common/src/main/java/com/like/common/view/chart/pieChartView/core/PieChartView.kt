package com.like.common.view.chart.pieChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.like.common.view.chart.pieChartView.entity.PieData
import com.like.logger.Logger

class PieChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mData: PieData? = null
    private val mConfig: PieChartConfig = PieChartConfig(context)

    private var totalWidth: Int = 0
    private var totalHeight: Int = 0
    private val pieRect: RectF by lazy {
        RectF(0f, 0f, totalWidth.toFloat(), totalHeight.toFloat())
    }

    private val mPiePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.BLACK)

        mPiePaint.style = Paint.Style.FILL
    }

    fun setData(data: PieData?) {
        if (data != null && data.monthDataList.isNotEmpty()) {
            mData = data
            mConfig.setData(data)
        }
        requestLayout()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        totalWidth = w
        totalHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        Logger.wtf("totalWidth = $totalWidth totalHeight = $totalHeight")
        //移动中心为整个View的中心，默认的是View的左上角
        canvas.translate((totalWidth / 2).toFloat(), (totalHeight / 2).toFloat())

        var startAngle = 0f
        val sweepAngle = 360f / 3f
        for (i in 0..2) {
            mPiePaint.color = PieChartConfig.DEFAULT_COLORS[i]
            canvas.drawRect(pieRect, mPiePaint)
            canvas.drawArc(pieRect, startAngle * sweepAngle, sweepAngle, true, mPiePaint)
        }
    }
}