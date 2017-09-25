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
import com.like.common.util.ImageLoaderUtils
import com.like.logger.Logger

class DragPhotoViewActivity : BaseActivity() {
    companion object {
        const val KEY_DATA = "dragPhotoViewInfoList"
    }

    private val mViewPager: FixMultiViewPager by lazy { FixMultiViewPager(this) }
    private val mPhotoViews = ArrayList<DragPhotoView>()

    private val mImageLoaderUtils: ImageLoaderUtils by lazy { ImageLoaderUtils(this) }

    private lateinit var dragPhotoViewInfoList: List<DragPhotoViewInfo>

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

        dragPhotoViewInfoList = intent.getSerializableExtra(KEY_DATA) as List<DragPhotoViewInfo>

        Logger.printCollection(dragPhotoViewInfoList)

        dragPhotoViewInfoList.mapTo(mPhotoViews) {
            DragPhotoView(this, it).apply {
                if (it.imageResId > 0) {
                    setImageResource(it.imageResId)
                } else {
                    mImageLoaderUtils.display(it.imageUrl, this)
                }
            }
        }

        mViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View?, `object`: Any?) = view === `object`
            override fun getCount() = mPhotoViews.size
            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                container?.addView(mPhotoViews[position])
                return mPhotoViews[position]
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container?.removeView(mPhotoViews[position])
            }
        }

        val curClickedIndex = getCurClickedIndex()
        if (curClickedIndex != -1) {
            mViewPager.currentItem = curClickedIndex
            mViewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    mPhotoViews[curClickedIndex].enter()
                }
            })
        }

        return null
    }

    private fun getCurClickedIndex(): Int {
        dragPhotoViewInfoList.forEachIndexed { index, dragPhotoViewInfo ->
            if (dragPhotoViewInfo.isClicked) {
                return index
            }
        }
        return -1
    }

    override fun onBackPressed() {
        mPhotoViews[mViewPager.currentItem].disappear()
    }
}
