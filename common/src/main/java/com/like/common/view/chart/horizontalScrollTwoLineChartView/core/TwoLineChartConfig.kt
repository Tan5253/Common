package com.like.common.view.chart.horizontalScrollTwoLineChartView.core

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.like.common.util.DimensionUtils
import com.like.common.util.DrawTextUtils
import com.like.common.view.chart.horizontalScrollTwoLineChartView.entity.TwoLineData

/**
 * 尺寸，常量
 */
class TwoLineChartConfig(val context: Context) {
    companion object {
        val DEFAULT_BG_COLOR = 0xffffffff.toInt()
        val DEFAULT_LINE_COLOR_1 = 0xffff9600.toInt()// 环比线颜色
        val DEFAULT_LINE_COLOR_2 = 0xff00a7ff.toInt()// 同比线颜色
        val DEFAULT_OTHER_LINE_COLOR = 0xffc0c0c0.toInt()// 其它线颜色

        val DEFAULT_TEXT_BG_COLOR_1 = 0xffff3b14.toInt()// 正数值背景颜色
        val DEFAULT_TEXT_BG_COLOR_2 = 0xff14d13a.toInt()// 负数值背景颜色
        val DEFAULT_TEXT_BG_COLOR_3 = 0xff9c9c9c.toInt()// 0值背景颜色

        val DEFAULT_TEXT_COLOR_0 = 0xff606060.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_1 = 0xffffffff.toInt()// 文本颜色
        val DEFAULT_TEXT_COLOR_2 = 0xffc0c0c0.toInt()// 文本颜色
    }

    // 文本字体大小
    val textSize = DimensionUtils.sp2px(context, 12f).toFloat()

    // 横坐标文本顶部的间隔
    val spacingXAxisTextTop: Float = DimensionUtils.dp2px(context, 8f).toFloat()
    // 线条图的高度
    val maxLineViewHeight: Float = DimensionUtils.dp2px(context, 130f).toFloat()
    // 线条图距离顶部的间隔
    val spacingLineViewTop: Float = DimensionUtils.dp2px(context, 40f).toFloat()
    // 线条图距离底部的间隔
    val spacingLineViewBottom: Float = DimensionUtils.dp2px(context, 50f).toFloat()
    // 点圆半径
    val pointCircleRadius: Float = DimensionUtils.dp2px(context, 2.5f).toFloat()
    // 触摸点的数值显示区域的宽
    val touchPointRectWidth: Float = DimensionUtils.dp2px(context, 40f).toFloat()
    // 触摸点的数值显示区域的高
    val touchPointRectHeight: Float = DimensionUtils.dp2px(context, 15f).toFloat()
    // 触摸点的数值显示区域的圆角半径
    val touchPointRectRadius: Float = DimensionUtils.dp2px(context, 3f).toFloat()

    // 视图总高度
    val totalHeight = spacingLineViewTop + maxLineViewHeight + spacingLineViewBottom

    val textPaint: Paint by lazy {
        val paint = Paint()
        paint.textSize = textSize
        paint
    }
    val textBaseLine: Float by lazy {
        DrawTextUtils.getTextBaseLine(textPaint)
    }
    val textHeight: Float by lazy {
        DrawTextUtils.getTextHeight(textPaint)
    }
    //  横坐标文本绘制的起点Y坐标
    val xAxisStartY: Float = spacingLineViewTop + maxLineViewHeight + spacingXAxisTextTop + textBaseLine
    // 两点之间的间隔
    var spacingBetweenTwoPoints: Float = 0f
    // 每个百分比(1%)对应的高度
    var eachRatioHeight: Float = 0f
    val screenWidthPixels: Float by lazy {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        metric.widthPixels.toFloat()
    }

    // 左面的那个"0.00%" 绘制的起点X坐标
    val middleLineLeftTextStartX: Float = DimensionUtils.dp2px(context, 10f).toFloat()
    // 左面的那个"0.00%" 绘制的起点Y坐标
    val middleLineLeftTextStartY: Float = spacingLineViewTop + maxLineViewHeight / 2 - textHeight / 2 + textBaseLine
    // 右面的那个"0.00%" 绘制的起点X坐标
    val middleLineRightTextStartX: Float by lazy {
        totalWidth - DrawTextUtils.getTextlength(textPaint, "0.00%") - middleLineLeftTextStartX
    }
    // 右面的那个"0.00%" 绘制的起点Y坐标
    val middleLineRightTextStartY: Float = middleLineLeftTextStartY
    // "暂无数据" 绘制的起点X坐标
    val middleLineMiddleTextStartX: Float by lazy {
        (screenWidthPixels - DrawTextUtils.getTextlength(textPaint, "暂无数据")) / 2
    }
    // "暂无数据" 绘制的起点Y坐标
    val middleLineMiddleTextStartY: Float = spacingLineViewTop + maxLineViewHeight / 2 - textHeight / 2

    // 环比path
    val path1: Path = Path()
    // 同比path
    val path2: Path = Path()
    // 所有数据
    val dataList: MutableList<TwoLineData> = arrayListOf()
    // 环比对应的所有点的坐标
    val pointList1: MutableList<PointF> = arrayListOf()
    // 同比对应的所有点的坐标
    val pointList2: MutableList<PointF> = arrayListOf()
    // 视图总宽度
    var totalWidth: Float = screenWidthPixels

    fun setData(twoLineDataList: List<TwoLineData>, touchPosition: Int, showPointCount: Int = 3) {
        if (showPointCount <= 0) {
            return
        }
        this.dataList.clear()
        this.dataList.addAll(twoLineDataList)
        this.touchPosition = touchPosition

        totalWidth = if (dataList.size > showPointCount) {
            val eachSpacing = screenWidthPixels / (showPointCount + 1)
            screenWidthPixels + eachSpacing * (dataList.size - showPointCount)
        } else {
            screenWidthPixels
        }

        spacingBetweenTwoPoints = totalWidth / (dataList.size + 1)

        val maxRatio1: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio1) }!!.ratio1)
        val maxRatio2: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio2) }!!.ratio2)
        if (hasLine1() && hasLine2()) {
            if (maxOf(maxRatio1, maxRatio2) != 0f) {
                eachRatioHeight = maxLineViewHeight / 2 / maxOf(maxRatio1, maxRatio2)
            }
        } else if (hasLine1()) {
            if (maxRatio1 != 0f) {
                eachRatioHeight = maxLineViewHeight / 2 / maxRatio1
            }
        } else if (hasLine2()) {
            if (maxRatio2 != 0f) {
                eachRatioHeight = maxLineViewHeight / 2 / maxRatio2
            }
        }

        path1.reset()
        pointList1.clear()
        pointList1.addAll(getAllPoint(1))

        path2.reset()
        pointList2.clear()
        pointList2.addAll(getAllPoint(2))
    }

    // 环比线上触摸点的数值显示区域
    val touchPointRect1 = RectF()
    // 同比线上触摸点的数值显示区域
    val touchPointRect2 = RectF()
    // 当前触摸位置
    var touchPosition = -1

    /**
     * 获取手指触摸点最接近的x坐标
     */
    fun getCurrentTouchPointX(touchX: Float): Float {
        val pointList = if (pointList1.isNotEmpty()) pointList1 else pointList2
        if (touchX != -1f) {// 真实触摸时才计算
            if (pointList.isNotEmpty()) {
                if (touchX <= pointList.first().x + spacingBetweenTwoPoints / 2) {
                    touchPosition = 0
                } else if (touchX >= pointList.last().x - spacingBetweenTwoPoints / 2) {
                    touchPosition = pointList.size - 1
                } else {
                    for ((index, pointF) in pointList.withIndex()) {
                        if (pointF.x - spacingBetweenTwoPoints / 2 <= touchX && pointF.x + spacingBetweenTwoPoints / 2 >= touchX) {
                            touchPosition = index
                            break
                        }
                    }
                }
            }
        }
        calcTouchPointRect()
        return if (touchPosition == -1) -1f else pointList[touchPosition].x
    }

    private fun calcTouchPointRect() {
        if (touchPosition != -1) {
            if (hasLine1()) {
                with(pointList1[touchPosition]) {
                    touchPointRect1.left = this.x - touchPointRectWidth / 2
                    touchPointRect1.top = this.y - touchPointRectHeight / 2
                    touchPointRect1.right = touchPointRect1.left + touchPointRectWidth
                    touchPointRect1.bottom = touchPointRect1.top + touchPointRectHeight
                }
            }
            if (hasLine2()) {
                with(pointList2[touchPosition]) {
                    touchPointRect2.left = this.x - touchPointRectWidth / 2
                    touchPointRect2.top = this.y - touchPointRectHeight / 2
                    touchPointRect2.right = touchPointRect2.left + touchPointRectWidth
                    touchPointRect2.bottom = touchPointRect2.top + touchPointRectHeight
                }
            }
        } else {
            touchPointRect1.left = 0f
            touchPointRect1.top = 0f
            touchPointRect1.right = 0f
            touchPointRect1.bottom = 0f

            touchPointRect2.left = 0f
            touchPointRect2.top = 0f
            touchPointRect2.right = 0f
            touchPointRect2.bottom = 0f
        }
    }

    fun isTouchInView(touchY: Float): Boolean = touchY >= spacingLineViewTop && touchY <= spacingLineViewTop + maxLineViewHeight

    /**
     * 获取所有折线的坐标点
     *
     * @param flag 1：折线1；2：折线2；
     */
    private fun getAllPoint(flag: Int): List<PointF> {
        if (flag != 1 && flag != 2) {// 这里只支持两条线
            return emptyList()
        }
        val result: MutableList<PointF> = mutableListOf()
        if (dataList.isNotEmpty()) {
            for ((index, twoLineData) in dataList.withIndex()) {
                val p = PointF()
                p.x = (index + 1) * spacingBetweenTwoPoints
                p.y = if (flag == 1) {
                    spacingLineViewTop + maxLineViewHeight / 2 - twoLineData.ratio1 * eachRatioHeight
                } else {
                    spacingLineViewTop + maxLineViewHeight / 2 - twoLineData.ratio2 * eachRatioHeight
                }
                if (flag == 1) {
                    if (index == 0) {
                        path1.moveTo(p.x, p.y)
                    } else {
                        path1.lineTo(p.x, p.y)
                    }
                } else {
                    if (index == 0) {
                        path2.moveTo(p.x, p.y)
                    } else {
                        path2.lineTo(p.x, p.y)
                    }
                }
                result.add(p)
            }
        }
        return result
    }

    fun hasLine1(): Boolean = dataList[0].ratio1 != Float.MAX_VALUE

    fun hasLine2(): Boolean = dataList[0].ratio2 != Float.MAX_VALUE

}