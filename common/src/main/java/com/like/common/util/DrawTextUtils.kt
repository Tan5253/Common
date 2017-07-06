package com.like.common.util

import android.graphics.Paint

object DrawTextUtils {
    /**
     * 返回指定笔和指定字符串的长度
     */
    @JvmStatic fun getTextlength(paint: Paint, text: String) = paint.measureText(text)

    /**
     * 获取绘制文本的起点Y坐标
     */
    @JvmStatic fun getTextBaseLine(paint: Paint) = (getTextHeight(paint) - paint.fontMetrics.descent - paint.fontMetrics.ascent) / 2

    /**
     * @return 返回指定笔的文字高度
     */
    @JvmStatic fun getTextHeight(paint: Paint) = paint.fontMetrics.bottom - paint.fontMetrics.top
}


