package com.like.common.view.chart.horizontalScrollBarChartView.entity

data class BarData(val month: Int, val electricity: Float)

fun getSimulatedData(): List<BarData> {
    return listOf(
            BarData(7, 7.7f),
            BarData(8, 8.8f),
            BarData(9, 9.9f),
            BarData(10, 11.0f),
            BarData(11, 12.1f),
            BarData(12, 13.1f),
            BarData(1, 1.1f),
            BarData(2, 2.2f),
            BarData(3, 3.3f),
            BarData(4, 4.4f),
            BarData(5, 5.5f),
            BarData(6, 6.6f)
    )
}