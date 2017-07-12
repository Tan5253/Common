package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData

/**
 * 可水平滑动的图表视图，只需要在xml文件中写上此视图即可用，必须调用init()方法
 */
class WrapHorizontalScrollTwoLineChartView(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {
    val twoLineChartView = TwoLineChartView(context)

    init {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        twoLineChartView.layoutParams = layoutParams
        addView(twoLineChartView)
    }

    /**
     * 设置数据
     *
     * @param dataList          数据
     * @param showPointCount    一屏幕显示的数据个数
     */
    fun setData(dataList: List<TwoLineData>, showPointCount: Int = 3) {
        if (showPointCount <= 0) {
            throw IllegalArgumentException("showPointCount 参数必须大于0")
        }
        twoLineChartView.setData(dataList, showPointCount)
    }
}
