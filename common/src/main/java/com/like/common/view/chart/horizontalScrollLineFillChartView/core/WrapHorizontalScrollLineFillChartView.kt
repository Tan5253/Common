package com.like.common.view.chart.horizontalScrollLineFillChartView.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import com.like.common.view.chart.horizontalScrollLineFillChartView.entity.LineData

/**
 * 可水平滑动的图表视图，只需要在xml文件中写上此视图即可用，必须调用init()方法
 */
class WrapHorizontalScrollLineFillChartView(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {
    val lineFillChartView = LineFillChartView(context)

    init {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        lineFillChartView.layoutParams = layoutParams
        addView(lineFillChartView)
    }

    fun setData(lineDataList: List<LineData>) {
        lineFillChartView.setData(lineDataList)
    }
}
