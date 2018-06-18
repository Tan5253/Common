package com.like.common.sample.banner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.like.common.sample.R
import com.like.common.util.GlideUtils
import com.like.common.view.banner.BaseBannerPagerAdapter
import com.like.toast.shortToastCenter

class BannerPagerAdapter(context: Context, val mList: List<BannerInfo>) : BaseBannerPagerAdapter(context) {
    private val layoutInflater = LayoutInflater.from(context)
    private val mGlideUtils = GlideUtils(context)

    override fun getView(container: ViewGroup?, position: Int): View {
        val view = layoutInflater.inflate(R.layout.item_banner, null)
        val iv = view.findViewById<ImageView>(R.id.iv)
        val dataPositon = position % mList.size
        val info = mList[dataPositon]
        mGlideUtils.display(info.imageUrl, iv)
        iv.setOnClickListener {
            context.shortToastCenter("hahah")
        }
        return view
    }

}
