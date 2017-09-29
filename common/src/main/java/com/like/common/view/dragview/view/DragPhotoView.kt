package com.like.common.view.dragview.view

import android.R
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.github.chrisbanes.photoview.PhotoView
import com.like.common.util.RxJavaUtils
import com.like.common.view.dragview.entity.DragInfo

class DragPhotoView(context: Context, val infos: List<DragInfo>) : BaseDragView(context, infos.filter { it.isClicked }[0]) {
    private val mViewPager: DragViewPager by lazy { DragViewPager(context) }
    private val mViews = ArrayList<RelativeLayout>()
    private val mPhotoViews = ArrayList<PhotoView>()
    private val mImageViews = ArrayList<ImageView>()
    private val mProgressBars = ArrayList<ProgressBar>()
    private var curClickedIndex = -1

    init {
        curClickedIndex = getCurClickedIndex()
        if (curClickedIndex != -1) {
            infos.forEach {
                val photoView = PhotoView(context).apply {
                    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    if (it.imageResId > 0) {
                        setImageResource(it.imageResId)
                    } else {
                        mImageLoaderUtils.display(it.imageUrl, this)
                    }
                }
                val imageView = ImageView(context).apply {
                    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    if (it.thumbImageResId > 0) {
                        setImageResource(it.thumbImageResId)
                    } else {
                        mImageLoaderUtils.display(it.thumbImageUrl, this)
                    }
                }
                val progressBar = ProgressBar(context, null, R.attr.progressBarStyleInverse).apply {
                    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        addRule(CENTER_IN_PARENT)
                    }
                }
                mViews.add(RelativeLayout(context).apply {
                    addView(imageView)
                    addView(progressBar)
                })
                mPhotoViews.add(photoView)
                mImageViews.add(imageView)
                mProgressBars.add(progressBar)
            }

            mViewPager.adapter = object : PagerAdapter() {
                override fun isViewFromObject(view: View?, `object`: Any?) = view === `object`
                override fun getCount() = mViews.size
                override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                    container?.addView(mViews[position])
                    return mViews[position]
                }

                override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                    container?.removeView(mViews[position])
                }
            }

            mViewPager.currentItem = curClickedIndex
            RxJavaUtils.timer(1000) {
                mViews[curClickedIndex].removeAllViews()
                mViews[curClickedIndex].addView(mPhotoViews[curClickedIndex])
            }

            mViewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    enter()
                }
            })
            mViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    curClickedIndex = position
                    mRestoreAnimationManager.setCurData(infos[curClickedIndex])
                    mDisappearAnimationManager.setCurData(infos[curClickedIndex])
                    mExitAnimationManager.setCurData(infos[curClickedIndex])
                    RxJavaUtils.timer(1000) {
                        mViews[position].removeAllViews()
                        mViews[position].addView(mPhotoViews[position])
                    }
                }
            })

            addView(mViewPager.apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                    addRule(CENTER_IN_PARENT)
                }
            })

        }

    }

    private fun getCurClickedIndex(): Int {
        infos.forEachIndexed { index, dragPhotoViewInfo ->
            if (dragPhotoViewInfo.isClicked) {
                return index
            }
        }
        return -1
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // 当scale == 1时才能drag
        if (scaleX == 1f && scaleY == 1f) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    onActionDown(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    // ViewPager的事件
                    if (mAnimationConfig.canvasTranslationY == 0f && mAnimationConfig.canvasTranslationX != 0f) {
                        return super.dispatchTouchEvent(event)
                    }

                    // 单手指按下，并在Y方向上拖动了一段距离
                    if (event.pointerCount == 1) {
                        mRestoreAnimationManager.updateCanvasTranslationX(event.x - mDownX)
                        mRestoreAnimationManager.updateCanvasTranslationY(event.y - mDownY)
                        mRestoreAnimationManager.updateCanvasScale()
                        mRestoreAnimationManager.updateCanvasBgAlpha()
                        invalidate()
                        return true
                    }

                    // 防止下拉的时候双手缩放
                    if (mAnimationConfig.canvasTranslationY >= 0f && mAnimationConfig.canvasScale < 0.95f) {
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    onActionUp(event)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}
