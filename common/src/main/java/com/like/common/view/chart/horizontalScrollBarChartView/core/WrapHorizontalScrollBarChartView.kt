package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.HorizontalScrollView

/**
 * 可水平滑动的图表视图，只需要在xml文件中写上此视图即可用，必须调用init()方法
 */
class WrapHorizontalScrollBarChartView(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {

    init {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        val barChartView = BarChartView(context)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        barChartView.layoutParams = layoutParams
        addView(barChartView)
    }

}
