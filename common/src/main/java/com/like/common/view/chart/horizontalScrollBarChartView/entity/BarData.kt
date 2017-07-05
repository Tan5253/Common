package com.like.common.view.chart.horizontalScrollBarChartView.entity

data class BarData(val month: Int, val electricity: Float, val isRealData: Boolean = true)

fun getSimulatedData(): List<BarData> {
    return listOf(
            BarData(0, 7.5f),
            BarData(1, 13.1f),
            BarData(2, 2.2f),
            BarData(3, 3.3f),
            BarData(4, 8.4f),
            BarData(5, 5.5f),
            BarData(6, 6.6f),
            BarData(7, 0.5f),
            BarData(8, 8.8f),
            BarData(9, 9.9f),
            BarData(10, 11.0f),
            BarData(11, 12.1f),
            BarData(12, 13.1f),
            BarData(13, 1.1f),
            BarData(14, 2.2f),
            BarData(15, 3.3f),
            BarData(16, 4.4f),
            BarData(17, 5.5f),
            BarData(18, 6.6f),
            BarData(19, 12.1f, false),
            BarData(20, 3.8f, false),
            BarData(21, 9.9f, false),
            BarData(22, 6.0f, false),
            BarData(23, 13.1f, false)
    )
}