package com.like.common.view.chart.pieChartView.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

class PieChartView(context: Context) : View(context) {
    private val mDataList: MutableList<LineData> = arrayListOf()
    private val mConfig: PieChartConfig = PieChartConfig(context)
    private lateinit var mDrawHelper: DrawHelper

    init {
        setBackgroundColor(Color.WHITE)

    }

    fun setData(lineDataList: List<LineData>) {

        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mConfig.totalWidth.toInt(), mConfig.totalHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        if (mDataList.isNotEmpty()) {
            mDrawHelper = DrawHelper(canvas, mConfig)

        }
    }
}