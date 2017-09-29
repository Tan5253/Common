package com.like.common.view.dragview.view

import android.R
import android.content.Context
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.VideoView
import com.like.common.util.RxJavaUtils
import com.like.common.view.dragview.entity.DragInfo

class DragVideoView(context: Context, info: DragInfo) : BaseDragView(context, info) {

    init {
        addView(ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            if (info.thumbImageResId > 0) {
                setImageResource(info.thumbImageResId)
            } else {
                mImageLoaderUtils.display(info.thumbImageUrl, this)
            }
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    enter()
                }
            })
        })

        addView(ProgressBar(context, null, R.attr.progressBarStyleInverse).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                addRule(CENTER_IN_PARENT)
            }
        })

        if (info.videoUrl.isNotEmpty()) {
            RxJavaUtils.timer(2000) {
                addView(VideoView(context).apply {
                    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                        addRule(CENTER_IN_PARENT)
                    }
                    setZOrderOnTop(true)// 避免闪屏
                    setVideoPath(info.videoUrl)
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = true
                        mediaPlayer.start()
                    }
                })
            }
        }

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
                        mRestoreAnimationManager.updateCanvasTranslationX(event.x - mDownX)
                        mRestoreAnimationManager.updateCanvasTranslationY(event.y - mDownY)
                        mRestoreAnimationManager.updateCanvasScale()
                        mRestoreAnimationManager.updateCanvasBgAlpha()
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