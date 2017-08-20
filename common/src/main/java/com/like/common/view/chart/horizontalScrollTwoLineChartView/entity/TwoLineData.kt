package com.like.common.view.chart.horizontalScrollTwoLineChartView.entity

/**
 * @param ratio2 当不传此值时，表示只有一条折线。
 */
data class TwoLineData(val x: Int, val ratio1: Float = Float.MAX_VALUE, val ratio2: Float = Float.MAX_VALUE)
