package com.like.common.view.dragphotoview

import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.R

class DragPhotoViewActivity : BaseActivity() {
    companion object {
        const val KEY_DATA = "DragPhotoViewInfo"
    }

    private val mViewPager: FixMultiViewPager by lazy { FixMultiViewPager(this) }
    private val mImageUrlList = listOf("url", "url", "url")
    private val mPhotoViews = ArrayList<DragPhotoView>()

    private lateinit var dragPhotoViewInfo: DragPhotoViewInfo

    override fun getViewModel(): BaseViewModel? {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getColor(int id)在API版本23时(Android 6.0)已然过时
            //从这里我们可以看出，当API版本>=23时，使用ContextCompatApi23.getColor(context, id)方法，
            //当API版本<23时，使用context.getResources().getColor(id)方法获取相应色值
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            window.statusBarColor = ContextCompat.getColor(this, R.color.common_transparent)
        }

        setContentView(mViewPager)

        dragPhotoViewInfo = intent.getSerializableExtra(KEY_DATA) as DragPhotoViewInfo

        mImageUrlList.mapTo(mPhotoViews) {
            DragPhotoView(this, dragPhotoViewInfo).apply {
                setImageResource(R.drawable.wugeng)
                mTapListener = object : OnTapListener {
                    override fun onTap(view: DragPhotoView) {
                        view.disappear()
                    }
                }
                mExitListener = object : OnExitListener {
                    override fun onExit(view: DragPhotoView, x: Float, y: Float, w: Float, h: Float) {
                        view.exit(x, y)
                    }

                    override fun onExitFinish() {
                        finish()
                        overridePendingTransition(0, 0)
                    }
                }
            }
        }

        mViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View?, `object`: Any?) = view === `object`
            override fun getCount() = mImageUrlList.size
            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                container?.addView(mPhotoViews[position])
                return mPhotoViews[position]
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container?.removeView(mPhotoViews[position])
            }
        }

        mViewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mPhotoViews[0].enter()
            }
        })
        return null
    }

    override fun onBackPressed() {
        mPhotoViews[mViewPager.currentItem].disappear()
    }
}
