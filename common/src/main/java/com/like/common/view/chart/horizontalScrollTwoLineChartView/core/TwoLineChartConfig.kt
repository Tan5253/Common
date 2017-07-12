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
        val DEFAULT_LINE_COLOR_1 = 0xff00a7ff.toInt()// 环比线颜色
        val DEFAULT_LINE_COLOR_2 = 0xffff9600.toInt()// 同比线颜色
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
    val maxLineViewHeight: Float = DimensionUtils.dp2px(context, 175f).toFloat()
    // 线条图距离顶部的间隔
    val spacingLineViewTop: Float = DimensionUtils.dp2px(context, 40f).toFloat()
    // 线条图距离底部的间隔
    val spacingLineViewBottom: Float = DimensionUtils.dp2px(context, 60f).toFloat()
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

    val textBaseLine: Float by lazy {
        val paint = Paint()
        paint.textSize = textSize
        DrawTextUtils.getTextBaseLine(paint)
    }
    val textHeight: Float by lazy {
        val paint = Paint()
        paint.textSize = textSize
        DrawTextUtils.getTextHeight(paint)
    }
    // "0.00%" 绘制的起点Y坐标
    val middleLineTextStartY: Float = spacingLineViewTop + maxLineViewHeight / 2 - textHeight / 2 + textBaseLine
    //  横坐标文本绘制的起点Y坐标
    val xAxisStartY: Float = spacingLineViewTop + maxLineViewHeight + spacingXAxisTextTop + textBaseLine
    // 所有数据
    val dataList: MutableList<TwoLineData> = arrayListOf()
    // 环比对应的所有点的坐标
    val pointList1: MutableList<PointF> = arrayListOf()
    // 同比对应的所有点的坐标
    val pointList2: MutableList<PointF> = arrayListOf()
    // 视图总宽度
    var totalWidth: Float = 0f
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

    // 环比path
    val path1: Path = Path()
    // 同比path
    val path2: Path = Path()

    fun setData(barDataList: List<TwoLineData>, showPointCount: Int = 3) {
        if (showPointCount <= 0) {
            return
        }
        this.dataList.clear()
        this.dataList.addAll(barDataList)

        totalWidth = if (dataList.size > showPointCount) {
            val eachSpacing = screenWidthPixels / (showPointCount + 1)
            screenWidthPixels + eachSpacing * (dataList.size - showPointCount)
        } else {
            screenWidthPixels
        }

        spacingBetweenTwoPoints = totalWidth / (dataList.size + 1)

        val maxRatio1: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio1) }!!.ratio1)
        val maxRatio2: Float = Math.abs(dataList.maxBy { Math.abs(it.ratio2) }!!.ratio2)

        eachRatioHeight = maxLineViewHeight / 2 / maxOf(maxRatio1, maxRatio2)

        path1.reset()
        path2.reset()

        pointList1.clear()
        pointList1.addAll(getAllPoint(1))

        pointList2.clear()
        pointList2.addAll(getAllPoint(2))

    }

    // 环比线上触摸点的数值显示区域
    val touchPointRect1 = RectF()
    // 同比线上触摸点的数值显示区域
    val touchPointRect2 = RectF()
    // 环比线上触摸点的数值
    var touchData1 = 0f
    // 同比线上触摸点的数值
    var touchData2 = 0f
    // 环比线上触摸点
    var touchPoint1: PointF? = null
    // 同比线上触摸点
    var touchPoint2: PointF? = null

    /**
     * 获取手指触摸点最接近的x坐标
     */
    fun getCurrentTouchPointX(touchX: Float): Float {
        var position: Int = -1
        if (pointList1.isNotEmpty()) {
            if (touchX <= pointList1.first().x + spacingBetweenTwoPoints / 2) {
                touchPoint1 = pointList1.first()
                position = 0
            } else if (touchX >= pointList1.last().x - spacingBetweenTwoPoints / 2) {
                touchPoint1 = pointList1.last()
                position = pointList1.size - 1
            } else {
                for ((index, pointF) in pointList1.withIndex()) {
                    if (pointF.x - spacingBetweenTwoPoints / 2 <= touchX && pointF.x + spacingBetweenTwoPoints / 2 >= touchX) {
                        touchPoint1 = pointF
                        position = index
                        break
                    }
                }
            }
        }
        if (touchPoint1 != null) {
            touchPointRect1.left = touchPoint1!!.x - touchPointRectWidth / 2
            touchPointRect1.top = touchPoint1!!.y - touchPointRectHeight / 2
            touchPointRect1.right = touchPointRect1.left + touchPointRectWidth
            touchPointRect1.bottom = touchPointRect1.top + touchPointRectHeight

            touchPoint2 = pointList2[position]
            touchPointRect2.left = touchPoint2!!.x - touchPointRectWidth / 2
            touchPointRect2.top = touchPoint2!!.y - touchPointRectHeight / 2
            touchPointRect2.right = touchPointRect2.left + touchPointRectWidth
            touchPointRect2.bottom = touchPointRect2.top + touchPointRectHeight

            touchData1 = dataList[position].ratio1
            touchData2 = dataList[position].ratio2
        } else {
            touchPointRect1.left = 0f
            touchPointRect1.top = 0f
            touchPointRect1.right = 0f
            touchPointRect1.bottom = 0f

            touchPointRect2.left = 0f
            touchPointRect2.top = 0f
            touchPointRect2.right = 0f
            touchPointRect2.bottom = 0f

            touchData1 = 0f
            touchData2 = 0f
            touchPoint2 = null
        }
        return if (touchPoint1 == null) -1f else touchPoint1!!.x
    }

    fun isTouchInView(touchY: Float): Boolean = touchY >= spacingLineViewTop && touchY <= spacingLineViewTop + maxLineViewHeight

    fun getAllPoint(flag: Int): List<PointF> {
        val result: MutableList<PointF> = mutableListOf()
        if (dataList.isNotEmpty()) {
            for ((index, twoLineData) in dataList.withIndex()) {
                val p: PointF = PointF()
                p.x = (index + 1) * spacingBetweenTwoPoints
                p.y = if (flag == 1) {
                    spacingLineViewTop + maxLineViewHeight / 2 - twoLineData.ratio1 * eachRatioHeight
                } else if (flag == 2) {
                    spacingLineViewTop + maxLineViewHeight / 2 - twoLineData.ratio2 * eachRatioHeight
                } else {
                    0f
                }
                if (flag == 1) {
                    if (index == 0) {
                        path1.moveTo(p.x, p.y)
                    } else {
                        path1.lineTo(p.x, p.y)
                    }
                } else if (flag == 2) {
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

}