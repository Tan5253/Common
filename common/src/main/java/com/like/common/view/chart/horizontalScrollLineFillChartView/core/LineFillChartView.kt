package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.graphics.*
import android.view.View
import com.like.common.view.chart.horizontalScrollBarChartView.core.BarChartConfig
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData
import com.like.logger.Logger


class LineFillChartView(context: Context) : View(context) {
    private val mLineDataList: MutableList<LineData> = arrayListOf()
    private val mLineFillChartConfig: LineFillChartConfig = LineFillChartConfig(context)
    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.WHITE)

        mLinePaint.style = Paint.Style.FILL
        mLinePaint.color = Color.BLACK
        mLinePaint.shader = LinearGradient(0f, 500f, 0f, 0f, BarChartConfig.DEFAULT_COLORS_REAL, BarChartConfig.DEFAULT_COLORS_POSITIONS, Shader.TileMode.CLAMP)
    }

    fun setData(lineDataList: List<LineData>) {
        mLineDataList.clear()
        mLineDataList.addAll(lineDataList)

        mLineFillChartConfig.setData(lineDataList)

        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mLineFillChartConfig.totalWidth, mLineFillChartConfig.totalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        Logger.wtf(mLineFillChartConfig.pathList[0])
        canvas.drawPath(mLineFillChartConfig.pathList[0], mLinePaint)
    }
}