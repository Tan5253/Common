package com.like.common.view.chart.horizontalScrollTwoLineChartView.entity

import com.like.common.util.MoneyFormatUtils

/**
 * @param ratio2 当不传此值时，表示只有一条折线。
 */
data class TwoLineData(val x: Int, val ratio1: Float = Float.MAX_VALUE, val ratio2: Float = Float.MAX_VALUE, val showData1: String = MoneyFormatUtils.formatTwoDecimals(Math.abs(ratio1).toDouble()), val showData2: String = MoneyFormatUtils.formatTwoDecimals(Math.abs(ratio2).toDouble()))
