package com.like.common.view.dragvideoview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.VideoView
import com.like.common.dragVideoView.dragvideodragVideoView.animation.DisappearAnimationManager
import com.like.common.util.ImageLoaderUtils
import com.like.common.util.RxJavaUtils
import com.like.common.view.dragvideoview.animation.EnterAnimationManager
import com.like.common.view.dragvideoview.animation.ExitAnimationManager
import com.like.common.view.dragvideoview.animation.RestoreAnimationManager

class DragVideoView(context: Context, dragVideoViewInfo: DragVideoViewInfo) : RelativeLayout(context) {
    private val mPaint: Paint = Paint().apply { color = Color.BLACK }

    private var mDownX: Float = 0f
    private var mDownY: Float = 0f

    private var mWidth: Float = 0f
    private var mHeight: Float = 0f

    internal val mRestoreAnimationManager: RestoreAnimationManager by lazy { RestoreAnimationManager(this, dragVideoViewInfo) }
    private val mEnterAnimationManager: EnterAnimationManager by lazy { EnterAnimationManager(this, dragVideoViewInfo) }
    private val mExitAnimationManager: ExitAnimationManager by lazy { ExitAnimationManager(this, dragVideoViewInfo) }
    private val mDisappearAnimationManager: DisappearAnimationManager by lazy { DisappearAnimationManager(this, dragVideoViewInfo) }

    private val mImageLoaderUtils: ImageLoaderUtils by lazy { ImageLoaderUtils(context) }

    init {
        setBackgroundColor(Color.BLACK)

        addView(ImageView(context).apply {
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            if (dragVideoViewInfo.imageResId > 0) {
                setImageResource(dragVideoViewInfo.imageResId)
            } else {
                mImageLoaderUtils.display(dragVideoViewInfo.imageUrl, this)
            }
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    enter()
                }
            })
        })

        if (dragVideoViewInfo.videoUrl.isNotEmpty()) {
            RxJavaUtils.timer(2000) {
                addView(VideoView(context).apply {
                    layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT).apply {
                        addRule(RelativeLayout.CENTER_IN_PARENT)
                    }
                    setZOrderOnTop(true)// 避免闪屏
                    setVideoPath(dragVideoViewInfo.videoUrl)
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = true
                        mediaPlayer.start()
                    }
                })
            }
        }

    }

    fun restore() {
        mRestoreAnimationManager.start()
    }

    fun disappear() {
        mDisappearAnimationManager.start()
    }

    fun enter() {
        mEnterAnimationManager.start()
    }

    fun exit(curTranslationX: Float, curTranslationY: Float) {
        mExitAnimationManager.setData(curTranslationX, curTranslationY).start()
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.alpha = mRestoreAnimationManager.canvasBgAlpha
        setBackgroundColor(Color.argb(mPaint.alpha, 0, 0, 0))
        canvas?.translate(mRestoreAnimationManager.canvasTranslationX, mRestoreAnimationManager.canvasTranslationY)
        canvas?.scale(mRestoreAnimationManager.canvasScale, mRestoreAnimationManager.canvasScale, mWidth / 2, mHeight / 2)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
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
        return true
    }

}