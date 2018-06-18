package com.like.common.view.banner

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

abstract class BaseBannerPagerAdapter(val context: Context) : PagerAdapter() {
    override fun getCount(): Int = Int.MAX_VALUE

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val view = getView(container, position)
        container?.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }

    abstract fun getView(container: ViewGroup?, position: Int): View
}
