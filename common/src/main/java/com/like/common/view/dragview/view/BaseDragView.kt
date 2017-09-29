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

open class BaseDragView(context: Context, info: DragInfo) : RelativeLayout(context) {
    protected val mPaint: Paint = Paint().apply { color = Color.BLACK }

    protected var mDownX: Float = 0f
    protected var mDownY: Float = 0f

    protected var mWidth: Float = 0f
    protected var mHeight: Float = 0f

    private var canFinish: Boolean = false

    protected val mConfig: AnimationConfig by lazy { AnimationConfig(info, this) }

    protected val mRestoreAnimationManager: RestoreAnimationManager by lazy { RestoreAnimationManager(mConfig) }
    protected val mEnterAnimationManager: EnterAnimationManager by lazy { EnterAnimationManager(mConfig) }
    protected val mExitAnimationManager: ExitAnimationManager by lazy { ExitAnimationManager(mConfig) }
    protected val mDisappearAnimationManager: DisappearAnimationManager by lazy { DisappearAnimationManager(mConfig) }

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

    fun exit() {
        mExitAnimationManager.start()
    }

    fun onActionDown(event: MotionEvent) {
        mDownX = event.x
        mDownY = event.y
        canFinish = !canFinish
    }

    fun onActionUp(event: MotionEvent) {
        // 防止下拉的时候双手缩放
        if (event.pointerCount == 1) {
            if (mConfig.curCanvasTranslationY > mConfig.MAX_CANVAS_TRANSLATION_Y) {
                exit()
            } else {
                restore()
            }
            // 延时判断是否可以退出，避免双击和单击冲突，造成PhotoView不能放大图片
            postDelayed({
                if (mConfig.curCanvasTranslationX == 0f && mConfig.curCanvasTranslationY == 0f && canFinish) {
                    disappear()
                }
                canFinish = false
            }, 200)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.alpha = mConfig.curCanvasBgAlpha
        setBackgroundColor(Color.argb(mPaint.alpha, 0, 0, 0))
        canvas?.translate(mConfig.curCanvasTranslationX, mConfig.curCanvasTranslationY)
        canvas?.scale(mConfig.curCanvasScale, mConfig.curCanvasScale, mWidth / 2, mHeight / 2)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

}