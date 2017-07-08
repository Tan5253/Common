package com.like.common.view.chart.pieChartView.entity

data class PieData(val year: Int, val quarter: Int, val monthDataList: List<MonthData>)

data class MonthData(val month: Int, val electricity: Float, val fee: Float)