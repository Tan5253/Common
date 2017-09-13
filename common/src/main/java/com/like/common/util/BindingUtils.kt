package com.like.common.util

import android.databinding.BindingAdapter
import android.widget.TextView
import com.like.common.view.chart.pieChartView.entity.MonthData
import com.like.common.view.chart.pieChartView.entity.PieData

object BindingUtils {

    // 显示年份文本
    @BindingAdapter("pieChartViewShowYear")
    @JvmStatic
    fun pieChartViewShowYear(tv: TextView, data: PieData?) {
        try {
            tv.text = if (data != null && data.year > 0) {
                data.year.toString()
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 显示季度文本
    @BindingAdapter("pieChartViewShowQuarter")
    @JvmStatic
    fun pieChartViewShowQuarter(tv: TextView, data: PieData?) {
        try {
            tv.text = when (data?.quarter) {
                1 -> "一季度"
                2 -> "二季度"
                3 -> "三季度"
                4 -> "四季度"
                else -> ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 显示月份文本
    @BindingAdapter("pieChartViewShowMonth0")
    @JvmStatic
    fun pieChartViewShowMonth0(tv: TextView, data: PieData?) {
        pieChartViewShowMonth(tv, data?.monthDataList?.get(0))
    }

    // 显示月份文本
    @BindingAdapter("pieChartViewShowMonth1")
    @JvmStatic
    fun pieChartViewShowMonth1(tv: TextView, data: PieData?) {
        pieChartViewShowMonth(tv, data?.monthDataList?.get(1))
    }

    // 显示月份文本
    @BindingAdapter("pieChartViewShowMonth2")
    @JvmStatic
    fun pieChartViewShowMonth2(tv: TextView, data: PieData?) {
        pieChartViewShowMonth(tv, data?.monthDataList?.get(2))
    }

    fun pieChartViewShowMonth(tv: TextView, monthData: MonthData?) {
        try {
            tv.text = if (monthData != null) {
                "${monthData.month}月"
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 显示年份占比标签文本
    @BindingAdapter("pieChartViewShowMonthRatioText0")
    @JvmStatic
    fun pieChartViewShowMonthRatioText0(tv: TextView, data: PieData?) {
        pieChartViewShowMonthRatioText(tv, data?.monthDataList?.get(0))
    }

    // 显示年份占比标签文本
    @BindingAdapter("pieChartViewShowMonthRatioText1")
    @JvmStatic
    fun pieChartViewShowMonthRatioText1(tv: TextView, data: PieData?) {
        pieChartViewShowMonthRatioText(tv, data?.monthDataList?.get(1))
    }

    // 显示年份占比标签文本
    @BindingAdapter("pieChartViewShowMonthRatioText2")
    @JvmStatic
    fun pieChartViewShowMonthRatioText2(tv: TextView, data: PieData?) {
        pieChartViewShowMonthRatioText(tv, data?.monthDataList?.get(2))
    }

    fun pieChartViewShowMonthRatioText(tv: TextView, monthData: MonthData?) {
        try {
            tv.text = if (monthData != null) {
                "${monthData.month}月占比"
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 显示年份占比文本
    @BindingAdapter("pieChartViewShowMonthRatio0")
    @JvmStatic
    fun pieChartViewShowMonthRatio0(tv: TextView, data: PieData?) {
        pieChartViewShowMonthRatio(tv, data?.monthDataList, 0)
    }

    // 显示年份占比文本
    @BindingAdapter("pieChartViewShowMonthRatio1")
    @JvmStatic
    fun pieChartViewShowMonthRatio1(tv: TextView, data: PieData?) {
        pieChartViewShowMonthRatio(tv, data?.monthDataList, 1)
    }

    // 显示年份占比文本
    @BindingAdapter("pieChartViewShowMonthRatio2")
    @JvmStatic
    fun pieChartViewShowMonthRatio2(tv: TextView, data: PieData?) {
        pieChartViewShowMonthRatio(tv, data?.monthDataList, 2)
    }

    fun pieChartViewShowMonthRatio(tv: TextView, monthDataList: List<MonthData>?, index: Int) {
        try {
            tv.text = if (monthDataList != null) {
                val totalElectricity = monthDataList.sumByDouble { it.data1.toDouble() }.toFloat()
                "${MoneyFormatUtils.formatTwoDecimals((monthDataList[index].data1 / totalElectricity).toDouble() * 100)}%"
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}