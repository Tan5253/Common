package com.like.common.util

import android.databinding.BindingAdapter
import android.widget.TextView
import com.like.common.view.chart.pieChartView.entity.PieData

object BindingUtils {

    // 显示年份文本
    @BindingAdapter("pieChartViewShowYear")
    @JvmStatic fun pieChartViewShowYear(tv: TextView, data: PieData?) {
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
    @JvmStatic fun pieChartViewShowQuarter(tv: TextView, data: PieData?) {
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

}