package com.like.common.view.dragview.view

import android.R
import android.content.Context
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import com.like.common.view.dragview.entity.DragInfo

class DragVideoView(context: Context, info: DragInfo) : BaseDragView(context, info) {
    private var loadVideoFailureCallBack: (() -> Unit)? = null
    private var loadVideoSuccessCallBack: (() -> Unit)? = null

    init {
        val imageView = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            if (info.thumbImageUrl.isNotEmpty()) {
                mImageLoaderUtils.display(info.thumbImageUrl, this)
            }
        }
        addView(imageView)

        val progressBar = ProgressBar(context, null, R.attr.progressBarStyleInverse).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                addRule(CENTER_IN_PARENT)
            }
        }
        addView(progressBar)

        if (info.videoUrl.isNotEmpty()) {
            addView(VideoView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                    addRule(CENTER_IN_PARENT)
                }
                setZOrderOnTop(true)// 避免闪屏
                setVideoPath(info.videoUrl)
                setOnPreparedListener { mediaPlayer ->
                    loadVideoSuccessCallBack = {
                        try {
                            mediaPlayer?.let {
                                mediaPlayer.isLooping = true
                                mediaPlayer.start()
                                postDelayed({
                                    removeView(imageView)
                                    removeView(progressBar)
                                }, 100)// 防闪烁
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    postDelayed(loadVideoSuccessCallBack, 2000)// 模拟加载视频数据
                }
                setOnErrorListener { _, _, _ ->
                    loadVideoFailureCallBack = {
                        removeView(progressBar)
                        Toast.makeText(context, "获取视频数据失败！", Toast.LENGTH_SHORT).show()
                    }
                    postDelayed(loadVideoFailureCallBack, 2000)// 模拟加载视频数据
                    true
                }
            })
        }

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                enter()
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(loadVideoSuccessCallBack)
        removeCallbacks(loadVideoFailureCallBack)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // 当scale == 1时才能drag
        if (scaleX == 1f && scaleY == 1f) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    onActionDown(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    // 单手指按下，并在Y方向上拖动了一段距离
                    if (event.pointerCount == 1) {
                        mConfig.updateCanvasTranslationX(event.x - mDownX)
                        mConfig.updateCanvasTranslationY(event.y - mDownY)
                        mConfig.updateCanvasScale()
                        mConfig.updateCanvasBgAlpha()
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    onActionUp(event)
                }
            }
        }
        return true
    }

}