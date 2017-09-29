package com.like.common.view.dragview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.like.common.util.ImageLoaderUtils
import com.like.common.view.dragview.animation.*
import com.like.common.view.dragview.entity.DragInfo

open class BaseDragView(context: Context, val info: DragInfo) : RelativeLayout(context) {
    protected val mPaint: Paint = Paint().apply { color = Color.BLACK }

    protected var mDownX: Float = 0f
    protected var mDownY: Float = 0f

    protected var mWidth: Float = 0f
    protected var mHeight: Float = 0f

    val mAnimationConfig: AnimationConfig = AnimationConfig()

    protected val mRestoreAnimationManager: RestoreAnimationManager by lazy { RestoreAnimationManager(this, info) }
    protected val mEnterAnimationManager: EnterAnimationManager by lazy { EnterAnimationManager(this, info) }
    protected val mExitAnimationManager: ExitAnimationManager by lazy { ExitAnimationManager(this, info) }
    protected val mDisappearAnimationManager: DisappearAnimationManager by lazy { DisappearAnimationManager(this, info) }

    protected val mImageLoaderUtils: ImageLoaderUtils by lazy { ImageLoaderUtils(context) }

    init {
        setBackgroundColor(Color.BLACK)
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
        mExitAnimationManager.setTranslationData(curTranslationX, curTranslationY).start()
    }

    fun onActionDown(event: MotionEvent) {
        mDownX = event.x
        mDownY = event.y
    }

    fun onActionUp(event: MotionEvent) {
        // 防止下拉的时候双手缩放
        if (event.pointerCount == 1) {
            if (mAnimationConfig.canvasTranslationX == 0f && mAnimationConfig.canvasTranslationY == 0f) {
                disappear()
            } else if (mAnimationConfig.canvasTranslationY > mRestoreAnimationManager.MAX_CANVAS_TRANSLATION_Y) {
                exit(mAnimationConfig.canvasTranslationX, mAnimationConfig.canvasTranslationY)
            } else {
                restore()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.alpha = mAnimationConfig.canvasBgAlpha
        setBackgroundColor(Color.argb(mPaint.alpha, 0, 0, 0))
        canvas?.translate(mAnimationConfig.canvasTranslationX, mAnimationConfig.canvasTranslationY)
        canvas?.scale(mAnimationConfig.canvasScale, mAnimationConfig.canvasScale, mWidth / 2, mHeight / 2)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

}