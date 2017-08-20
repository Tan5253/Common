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
     * @param twoLineDataList   数据
     * @param touchPosition     初始触摸位置
     * @param showPointCount    一屏幕显示的数据个数
     */
    fun setData(twoLineDataList: List<TwoLineData>, touchPosition: Int = -1, showPointCount: Int = 3) {
        if (showPointCount <= 0) {
            throw IllegalArgumentException("showPointCount 参数必须大于0")
        }
        if (touchPosition >= twoLineDataList.size) {
            throw IllegalArgumentException("touchPosition 参数必须小于dataList中的数据个数")
        }

        twoLineChartView.setData(twoLineDataList, touchPosition, showPointCount)
        if (touchPosition != -1) {// 如果有初始值，就使这个值处于屏幕中间
            val currentTouchPositionX = (twoLineChartView.mConfig.pointList1[touchPosition].x.toInt()
                    - twoLineChartView.mConfig.screenWidthPixels / 2).toInt()
            // 这里必须用post，因为必须在HorizontalScrollView绘制完成后scrollTo()方法才会有效
            this.post({ this.scrollTo(currentTouchPositionX, 0) })
        }
    }
}
