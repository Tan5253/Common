package com.like.common.view.dragview.view

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.github.chrisbanes.photoview.PhotoView
import com.like.common.view.dragview.entity.DragInfo

class DragPhotoView(context: Context, val infos: List<DragInfo>) : BaseDragView(context, infos.filter { it.isClicked }[0]) {
    private val mViewPager: DragViewPager by lazy { DragViewPager(context) }
    private val mPhotoViews = ArrayList<PhotoView>()

    init {
        val curClickedIndex = getCurClickedIndex()
        if (curClickedIndex != -1) {
            infos.mapTo(mPhotoViews) {
                PhotoView(context).apply {
                    if (it.thumbImageResId > 0) {
                        setImageResource(it.thumbImageResId)
                    } else {
                        mImageLoaderUtils.display(it.thumbImageUrl, this)
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

            mViewPager.currentItem = curClickedIndex
            mViewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    enter()
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
                    mDownX = event.x
                    mDownY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    // ViewPager的事件
                    if (mRestoreAnimationManager.canvasTranslationY == 0f && mRestoreAnimationManager.canvasTranslationX != 0f) {
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
                    if (mRestoreAnimationManager.canvasTranslationY >= 0f && mRestoreAnimationManager.canvasScale < 0.95f) {
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // 防止下拉的时候双手缩放
                    if (event.pointerCount == 1) {
                        if (mRestoreAnimationManager.canvasTranslationX == 0f && mRestoreAnimationManager.canvasTranslationY == 0f) {
                            disappear()
                        } else if (mRestoreAnimationManager.canvasTranslationY > mRestoreAnimationManager.MAX_CANVAS_TRANSLATION_Y) {
                            exit(mRestoreAnimationManager.canvasTranslationX, mRestoreAnimationManager.canvasTranslationY)
                        } else {
                            restore()
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}
