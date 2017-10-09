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
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.like.common.view.dragview.entity.DragInfo
import java.lang.Exception

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
                }
                val imageView = ImageView(context).apply {
                    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
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
            showOriginImage(curClickedIndex)

            mViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    curClickedIndex = position
                    mConfig.setData(infos[curClickedIndex])
                    showOriginImage(curClickedIndex)
                }
            })

            addView(mViewPager.apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                    addRule(CENTER_IN_PARENT)
                }
            })

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    enter()
                }
            })
        }
    }

    private fun showOriginImage(index: Int) {
        // 延迟加载原始图片，避免闪烁
        val info = infos[index]
        val photoView = mPhotoViews[index]
        val progressBar = mProgressBars[index]
        val imageView = mImageViews[index]

        if (info.thumbImageUrl.isNotEmpty()) {
            mImageLoaderUtils.display(info.thumbImageUrl, imageView, object : RequestListener<String, GlideBitmapDrawable> {
                override fun onException(e: Exception?, model: String?, target: Target<GlideBitmapDrawable>?, isFirstResource: Boolean): Boolean {
                    mViews[index].removeView(progressBar)
                    Toast.makeText(context, "获取图片数据失败！", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(resource: GlideBitmapDrawable?, model: String?, target: Target<GlideBitmapDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    if (info.imageUrl.isNotEmpty()) {
                        mViews[index].addView(photoView)
                        mImageLoaderUtils.display(info.imageUrl, photoView, object : RequestListener<String, GlideBitmapDrawable> {
                            override fun onException(e: Exception?, model: String?, target: Target<GlideBitmapDrawable>?, isFirstResource: Boolean): Boolean {
                                mViews[index].removeView(progressBar)
                                mViews[index].removeView(photoView)
                                Toast.makeText(context, "获取图片数据失败！", Toast.LENGTH_SHORT).show()
                                return false
                            }

                            override fun onResourceReady(resource: GlideBitmapDrawable?, model: String?, target: Target<GlideBitmapDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                postDelayed({
                                    mViews[index].removeView(progressBar)
                                    mViews[index].removeView(imageView)
                                }, 100)// 防闪烁
                                return false
                            }
                        })
                    }
                    return false
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
                    if (mConfig.curCanvasTranslationY == 0f && mConfig.curCanvasTranslationX != 0f) {
                        return super.dispatchTouchEvent(event)
                    }

                    // 单手指按下，并在Y方向上拖动了一段距离
                    if (event.pointerCount == 1) {
                        mConfig.updateCanvasTranslationX(event.x - mDownX)
                        mConfig.updateCanvasTranslationY(event.y - mDownY)
                        mConfig.updateCanvasScale()
                        mConfig.updateCanvasBgAlpha()
                        invalidate()
                        return true
                    }

                    // 防止下拉的时候双手缩放
                    if (mConfig.curCanvasTranslationY >= 0f && mConfig.curCanvasScale < 0.95f) {
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
