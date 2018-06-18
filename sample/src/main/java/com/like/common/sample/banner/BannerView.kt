package com.like.common.sample.banner

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import com.like.common.sample.R
import com.like.common.util.PhoneUtils
import com.like.common.view.banner.BannerViewPager
import com.like.common.view.transformerviewpager.FixedSpeedScroller
import com.like.common.view.transformerviewpager.transformer.RotateYTransformer
import com.like.common.view.banner.BaseBannerPagerAdapter
import com.like.common.view.banner.BaseBannerView

class BannerView(context: Context, attrs: AttributeSet?) : BaseBannerView<BannerInfo>(context, attrs) {
    override fun getAdapter(vp: BannerViewPager, list: List<BannerInfo>): BaseBannerPagerAdapter {
        // 设置切换动画
        vp.setPageTransformer(true, object : RotateYTransformer() {
            override fun getRotate(context: Context): Float {
                var rotate = 0.5f
                val densityDpi = PhoneUtils.getInstance(context).mPhoneStatus.densityDpi
                if (densityDpi <= 240) {
                    rotate = 3f
                } else if (densityDpi <= 320) {
                    rotate = 2f
                }
                return rotate
            }
        })
        setViewPagerSpeed(vp, 250)
        return BannerPagerAdapter(context, list)
    }

    override fun getViewLayoutId() = R.layout.view_banner

    /**
     * 设置ViewPager切换速度
     *
     * @param viewPager
     * @param duration
     */
    private fun setViewPagerSpeed(viewPager: ViewPager, duration: Int) {
        try {
            val field = ViewPager::class.java.getDeclaredField("mScroller")
            field.isAccessible = true
            val scroller = FixedSpeedScroller(context, AccelerateInterpolator())
            field.set(viewPager, scroller)
            scroller.setmDuration(duration)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}
