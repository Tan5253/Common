package com.like.common.view.chart.pieChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.PointF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.like.common.util.DimensionUtils
import com.like.common.util.DrawTextUtils
import com.like.common.view.chart.pieChartView.entity.PieData

/**
 * 尺寸，常量
 */
class PieChartConfig(val context: Context) {
    companion object {
        val DEFAULT_COLORS = intArrayOf(// 填充颜色数组
                0xffff5581.toInt(),
                0xff00ccff.toInt(),
                0xffffcc00.toInt()
        )
        val DEFAULT_BOTTOM_BG_COLOR = 0xff28c4ff.toInt()// 底部矩形背景颜色
        val DEFAULT_TEXT_COLOR_0 = 0xff606060.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_1 = 0xff606060.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_2 = 0xff606060.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_3 = 0xff303030.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_4 = 0xff606060.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_5 = 0xffbde9ff.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_6 = 0xffffffff.toInt()// 文本颜色
    }

    // 文本字体大小
    val textSize0 = DimensionUtils.sp2px(context, 14f).toFloat()
    // 文本字体大小
    val textSize1 = DimensionUtils.sp2px(context, 16f).toFloat()
    // 文本字体大小
    val textSize2 = DimensionUtils.sp2px(context, 12f).toFloat()
    // 文本字体大小
    val textSize3 = DimensionUtils.sp2px(context, 18f).toFloat()
    // 文本字体大小
    val textSize4 = DimensionUtils.sp2px(context, 18f).toFloat()
    // 文本字体大小
    val textSize5 = DimensionUtils.sp2px(context, 12f).toFloat()
    // 文本字体大小
    val textSize6 = DimensionUtils.sp2px(context, 14f).toFloat()

    // 底部文本区域上下留白
    val spacingOnBottomTextAreaTopOrBottom: Float = DimensionUtils.dp2px(context, 8f).toFloat()
    // 底部文本区域两行文本之间的间隙
    val spacingOnBottomTextAreaBetweenTwoText: Float = DimensionUtils.dp2px(context, 3f).toFloat()

    // 左面间隔
    val spacingLeft: Float = DimensionUtils.dp2px(context, 20f).toFloat()
    // 顶部间隔
    val spacingTop: Float = DimensionUtils.dp2px(context, 30f).toFloat()
    // 右面间隔
    val spacingRight: Float = DimensionUtils.dp2px(context, 20f).toFloat()
    // 底部间隔
    val spacingBottom: Float = DimensionUtils.dp2px(context, 40f).toFloat()
    // 中间间隔
    val spacingMiddle: Float = DimensionUtils.dp2px(context, 30f).toFloat()

    // 圆饼半径
    val pieRadius: Float by lazy {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        (metric.widthPixels - spacingLeft - spacingMiddle - spacingRight) * 5 / 9f
    }
    // 圆饼中心点
    val pieCenterPoint: PointF = PointF(spacingLeft + pieRadius / 2, spacingTop + pieRadius / 2)
    // 圆饼右面的文本区域宽
    val rightTextAreaWidth: Float = pieRadius * 4 / 5
    // 圆饼下面的文本区域高度
    val bottomTextAreaHeight: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = textSize5
        val text5Height = DrawTextUtils.getTextHeight(paint)
        paint.textSize = textSize6
        val text6Height = DrawTextUtils.getTextHeight(paint)
        spacingOnBottomTextAreaTopOrBottom * 2 + text5Height + spacingOnBottomTextAreaBetweenTwoText + text6Height
    }
    // 视图总高度
    val totalHeight = spacingTop + pieRadius + spacingBottom + bottomTextAreaHeight
    // 视图总宽度
    val totalWidth: Float by lazy {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        metric.widthPixels.toFloat()
    }

    val paint: Paint = Paint()
    // 文本绘制的起点Y坐标
    val textStartY0: Float by lazy {
        paint.textSize = textSize0
        pieCenterPoint.y - 10f - DrawTextUtils.getTextHeight(paint) + DrawTextUtils.getTextBaseLine(paint)
    }
    // 文本绘制的起点Y坐标
    val textStartY1: Float by lazy {
        paint.textSize = textSize1
        pieCenterPoint.y + 10f + DrawTextUtils.getTextBaseLine(paint)
    }

    // 第一段圆饼扇形的占圆的比例
    var pieRatio0: Float = 0f
    // 第二段圆饼扇形的占圆的比例
    var pieRatio1: Float = 0f
    // 第三段圆饼扇形的占圆的比例
    var pieRatio2: Float = 0f
    // 所有数据
    val dataList: MutableList<PieData> = arrayListOf()

    fun setData(dataList: List<PieData>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)

        val totalElectricity = dataList.sumByDouble { it.electricity.toDouble() }
        pieRatio0 = dataList[0].electricity / totalElectricity.toFloat()
        pieRatio1 = dataList[1].electricity / totalElectricity.toFloat()
        pieRatio2 = dataList[2].electricity / totalElectricity.toFloat()
    }

}