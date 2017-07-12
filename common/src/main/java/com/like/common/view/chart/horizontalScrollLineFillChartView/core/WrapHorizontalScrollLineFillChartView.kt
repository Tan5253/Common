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

    /**
     * 设置数据
     *
     * @param dataList          数据
     * @param showPointCount    一屏幕显示的数据个数
     */
    fun setData(dataList: List<LineData>, showPointCount: Int = 3) {
        if (showPointCount <= 0) {
            throw IllegalArgumentException("showPointCount 参数必须大于0")
        }
        lineFillChartView.setData(dataList, showPointCount)
    }
}
