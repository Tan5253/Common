package com.like.common.util

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView

/**
 * 注意包头不包尾
 */
object TextViewUtils {

    fun setSpan(tv: TextView, content: String, startEndPairs: List<Pair<Int, Int>>, @ColorRes colors: List<Int>) {
        if (startEndPairs.size != colors.size) {
            tv.text = content
            return
        }
        val string = SpannableString(content)
        val context = tv.context
        startEndPairs.forEachIndexed { index, pair ->
            val span = ForegroundColorSpan(ContextCompat.getColor(context, colors[index]))
            string.setSpan(span, pair.first, pair.second, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tv.text = string
    }

    fun setSpan(tv: TextView, content: String, start: Int, end: Int, @ColorRes color: Int) {
        val string = SpannableString(content)
        string.setSpan(ForegroundColorSpan(tv.context.resources.getColor(color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = string
    }

    fun setSpan(tv: TextView, content: String, positions: IntArray, @ColorRes color: Int) {
        val string = SpannableString(content)
        positions.forEach {
            string.setSpan(ForegroundColorSpan(tv.context.resources.getColor(color)), it, it + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tv.text = string
    }
}
