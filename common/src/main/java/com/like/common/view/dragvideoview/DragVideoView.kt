package com.like.common.view.dragvideoview

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.like.common.R
import com.like.common.databinding.ViewDragVideoBinding
import com.like.common.dragVideoView.dragvideodragVideoView.animation.DisappearAnimationManager
import com.like.common.util.ImageLoaderUtils
import com.like.common.util.PhoneUtils
import com.like.common.util.RxJavaUtils
import com.like.common.view.dragvideoview.animation.EnterAnimationManager
import com.like.common.view.dragvideoview.animation.ExitAnimationManager
import com.like.common.view.dragvideoview.animation.RestoreAnimationManager

class DragVideoView(context: Context, dragVideoViewInfo: DragVideoViewInfo) : FrameLayout(context) {
    private val mPaint: Paint = Paint().apply { color = Color.BLACK }

    private var mDownX: Float = 0f
    private var mDownY: Float = 0f

    private val screenWidth = PhoneUtils.getInstance(context).mPhoneStatus.screenWidth.toFloat()
    private val screenHeight = PhoneUtils.getInstance(context).mPhoneStatus.screenHeight.toFloat()
    private var mWidth: Float = 0f
    private var mHeight: Float = 0f

    private var canFinish: Boolean = false

    internal val mRestoreAnimationManager: RestoreAnimationManager by lazy { RestoreAnimationManager(this, dragVideoViewInfo) }
    private val mEnterAnimationManager: EnterAnimationManager by lazy { EnterAnimationManager(this, dragVideoViewInfo) }
    private val mExitAnimationManager: ExitAnimationManager by lazy { ExitAnimationManager(this, dragVideoViewInfo) }
    private val mDisappearAnimationManager: DisappearAnimationManager by lazy { DisappearAnimationManager(this, dragVideoViewInfo) }

    private val mImageLoaderUtils: ImageLoaderUtils by lazy { ImageLoaderUtils(context) }

    init {
        val binding = DataBindingUtil.inflate<ViewDragVideoBinding>(LayoutInflater.from(context), R.layout.view_drag_video, this, true)

        binding.videoView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.videoView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (dragVideoViewInfo.imageResId > 0) {
                    binding.iv.setImageResource(dragVideoViewInfo.imageResId)
                } else {
                    mImageLoaderUtils.display(dragVideoViewInfo.imageUrl, binding.iv)
                }

                if (dragVideoViewInfo.videoUrl.isNotEmpty()) {
                    RxJavaUtils.timer(3000) {
                        binding.iv.visibility = View.GONE
                        binding.progressbar.visibility = View.GONE
                        binding.videoView.visibility = View.VISIBLE
                        binding.videoView.setVideoPath(dragVideoViewInfo.videoUrl)
                        binding.videoView.setOnPreparedListener { mediaPlayer ->
                            mediaPlayer.start()
                            mediaPlayer.isLooping = true
                        }
                    }
                }

                enter()
            }
        })
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
        canvas?.drawRect(0f, 0f, screenWidth, screenHeight, mPaint)
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
                    canFinish = !canFinish
                }
                MotionEvent.ACTION_MOVE -> {
                    // 单手指按下，并在Y方向上拖动了一段距离
                    if (mRestoreAnimationManager.canvasTranslationY >= 0f && event.pointerCount == 1) {
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
                        if (mRestoreAnimationManager.canvasTranslationY > mRestoreAnimationManager.MAX_CANVAS_TRANSLATION_Y) {
                            exit(mRestoreAnimationManager.canvasTranslationX, mRestoreAnimationManager.canvasTranslationY)
                        } else {
                            restore()
                        }
                        // 延时判断是否可以退出
                        postDelayed({
                            if (mRestoreAnimationManager.canvasTranslationX == 0f && mRestoreAnimationManager.canvasTranslationY == 0f && canFinish) {
                                disappear()
                            }
                            canFinish = false
                        }, 300)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}