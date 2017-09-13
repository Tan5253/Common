package com.like.common.view.chart.pieChartView.entity

data class PieData(val year: Int, val quarter: Int, val monthDataList: List<MonthData>)

data class MonthData(val month: Int, val data1: Float, val data2: Float, val showData1: String, val showData2: String)