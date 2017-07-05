package com.like.common.view.chart.horizontalScrollBarChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.like.common.util.DimensionUtils
import com.like.common.view.chart.horizontalScrollBarChartView.entity.BarData

/**
 * 尺寸，常量
 */
class BarChartConfig(val context: Context, val barDataList: List<BarData>) {
    companion object {
        val DEFAULT_TEXT_BG_COLOR_REAL = 0xff28c4ff.toInt()// 文本区域背景颜色，真实数据
        val DEFAULT_TEXT_BG_COLOR = 0xffe5f8ff.toInt()// 文本区域背景颜色，预测数据
        val DEFAULT_MONTH_TEXT_COLOR_REAL = 0xffbde9ff.toInt()// 月份数据文本颜色，真实数据
        val DEFAULT_MONTH_TEXT_COLOR = 0xff9c9c9c.toInt()// 月份数据文本颜色，预测数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR_REAL = 0xffffffff.toInt()// 电量数据文本颜色，真实数据
        val DEFAULT_ELECTRICITY_TEXT_COLOR = 0xff303030.toInt()// 电量数据文本颜色，预测数据
        val DEFAULT_OTHER_TEXT_COLOR = 0xff9c9c9c.toInt()// "预测"文本
        val DEFAULT_COLORS_REAL = intArrayOf(// 柱形图颜色数组，真实数据
                0xff02bbff.toInt(),
                0xffa845e7.toInt(),
                0xffed4b90.toInt(),
                0xfff84330.toInt()
        )
        val DEFAULT_COLOR = 0xff9c9c9c.toInt()// 柱形图颜色，预测数据
        val DEFAULT_COLORS_POSITIONS = floatArrayOf(// 颜色对应的终点位置的数组
                0.4f, 0.7f, 0.9f, 1.0f
        )

        val DEFAULT_BAR_COUNT_IN_SCREEN = 10// 一屏幕显示几个柱形图
    }

    val displayMetrics: DisplayMetrics by lazy {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        metric
    }
    // 屏幕宽度
    val screenWidth: Int = displayMetrics.widthPixels
    // 屏幕高度
    val screenHeight: Int = displayMetrics.heightPixels


    val eachBarWidth: Float = 40f// 每个柱形图的宽度
    val totalBarHeight: Float = 500f// 柱形图高度
    val spacingBetweenTwoBars: Float = 90f// 两个柱形图之间的间隔
    val spacingBetweenBarAndTextArea: Float = 50f// 柱形图和文本区域之间的间隔
    val spacingBarTop: Float = 50f// 柱形图距离顶部的间隔
    val spacingOnTextTopOrBottom: Float = 20f// 文本区域上下留白
    val spacingBetweenTwoText: Float = 20f// 月份数据和电量数据之间的间隙


    // 视图总宽度
    val totalWidth = (eachBarWidth * barDataList.size + spacingBetweenTwoBars * barDataList.size).toInt()
    // 所有柱形图的Rect
    val barRectList: List<RectF> = BarChartHelper.getBarRectList(barDataList, this)
    // "预测"两个字的字体大小
    val otherTextSize = DimensionUtils.sp2px(context, 9f).toFloat()
    // 月份数据文本字体大小
    val monthTextSize = DimensionUtils.sp2px(context, 9f).toFloat()
    // 电量数据文本字体大小
    val electricityTextSize = DimensionUtils.sp2px(context, 12f).toFloat()
    // 月份数据文本绘制的起点Y坐标
    val monthTextStartY: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        spacingBarTop + totalBarHeight + spacingBetweenBarAndTextArea + getFontY(paint)
    }
    // 电量数据文本绘制的起点Y坐标
    val electricityTextStartY: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        val electricityTextTop = spacingBarTop + totalBarHeight + spacingBetweenBarAndTextArea + getFontHeight(paint)
        paint.textSize = electricityTextSize
        electricityTextTop + getFontY(paint)
    }
    // 文本区域总高度
    val totalTextHeight: Float by lazy {
        val paint: Paint = Paint()
        paint.textSize = monthTextSize
        val monthTextHeight = getFontHeight(paint)
        paint.textSize = electricityTextSize
        val electricityTextHeight = getFontHeight(paint)
        spacingOnTextTopOrBottom * 2 + spacingBetweenTwoText + monthTextHeight + electricityTextHeight
    }
    // 视图总高度
    val totalHeight = (spacingBarTop + totalBarHeight + spacingBetweenBarAndTextArea + totalTextHeight).toInt()
    // 柱形图的圆角半径
    val barRadius = eachBarWidth / 3
    // 已出数据的文本区域背景Rect
    val textBgRect = RectF(0f, spacingBarTop + totalBarHeight + spacingBetweenBarAndTextArea, totalWidth.toFloat(), totalHeight.toFloat())

    /**
     * @return 返回指定笔和指定字符串的长度
     */
    fun getFontlength(paint: Paint, str: String) = paint.measureText(str)

    /**
     * 获取绘制文本的起点Y坐标
     */
    fun getFontY(paint: Paint) = getFontHeight(paint) / 2 + getFontLeading(paint)

    /**
     * @return 返回指定笔的文字高度
     */
    fun getFontHeight(paint: Paint) = paint.fontMetrics.descent - paint.fontMetrics.ascent

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    fun getFontLeading(paint: Paint) = paint.fontMetrics.leading - paint.fontMetrics.ascent

}