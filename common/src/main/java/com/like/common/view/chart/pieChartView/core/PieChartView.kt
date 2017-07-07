package com.like.common.view.chart.pieChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.like.common.view.chart.pieChartView.entity.PieData

class PieChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mData: PieData? = null
    private val mConfig: PieChartConfig = PieChartConfig(context)

    private var totalWidth: Float = 0f
    private var totalHeight: Float = 0f
    private val pieRect: RectF by lazy {
        RectF(0f, 0f, totalWidth, totalHeight)
    }

    private val mPiePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(PieChartConfig.DEFAULT_BG_COLOR)

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
        totalWidth = w.toFloat()
        totalHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 0..2) {
            mPiePaint.color = PieChartConfig.DEFAULT_COLORS[i]
            canvas.drawArc(pieRect, mConfig.startAngle[i], mConfig.sweepAngle[i], true, mPiePaint)
        }
        mPiePaint.color = PieChartConfig.DEFAULT_BG_COLOR
        canvas.drawCircle(totalWidth / 2, totalHeight / 2, totalWidth / 2 - mConfig.ringWidth, mPiePaint)
    }
}