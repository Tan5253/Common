package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.*
import android.view.View
import com.like.common.view.chart.horizontalScrollBarChartView.core.BarChartConfig


class LineFillChartView(context: Context) : View(context) {
    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.WHITE)

        mLinePaint.style = Paint.Style.FILL
        mLinePaint.color = Color.BLACK
        mLinePaint.shader = LinearGradient(0f, 500f, 0f, 0f, BarChartConfig.DEFAULT_COLORS_REAL, BarChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(500, 500)
    }

    override fun onDraw(canvas: Canvas) {
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(50f, 50f)
        path.lineTo(50f, 500f)
        path.lineTo(0f, 500f)
        canvas.drawPath(path, mLinePaint)
    }
}