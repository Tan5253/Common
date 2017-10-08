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
import com.like.logger.Logger


open class BaseDragView(context: Context, info: DragInfo) : RelativeLayout(context) {
    private val mPaint: Paint = Paint().apply { color = Color.BLACK }

    protected var mDownX = 0f
    protected var mDownY = 0f
    // 辅助判断单击、双击、长按事件
    private val DOUBLE_CLICK_INTERVAL = 300L
    private var firstClickTime = 0L
    private var secondClickTime = 0L
    private var isUp = false
    private var isDoubleClick = false

    private var mWidth = 0f
    private var mHeight = 0f

    protected val mConfig: AnimationConfig by lazy { AnimationConfig(info, this) }

    private val mRestoreAnimationManager: RestoreAnimationManager by lazy { RestoreAnimationManager(mConfig) }
    private val mEnterAnimationManager: EnterAnimationManager by lazy { EnterAnimationManager(mConfig) }
    private val mExitAnimationManager: ExitAnimationManager by lazy { ExitAnimationManager(mConfig) }
    private val mDisappearAnimationManager: DisappearAnimationManager by lazy { DisappearAnimationManager(mConfig) }

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
        isUp = false
        if (firstClickTime == 0L && secondClickTime == 0L) {//第一次点击
            firstClickTime = System.currentTimeMillis()
            postDelayed({
                if (!isUp) {
                    Logger.v("长按")
                } else if (!isDoubleClick) {
                    Logger.v("单击")
                    if (mConfig.curCanvasTranslationX == 0f && mConfig.curCanvasTranslationY == 0f) {
                        disappear()
                    }
                }
                isDoubleClick = false
                firstClickTime = 0L
                secondClickTime = 0L
            }, DOUBLE_CLICK_INTERVAL)
        } else {
            secondClickTime = System.currentTimeMillis()
            if (secondClickTime - firstClickTime < DOUBLE_CLICK_INTERVAL) {//两次点击小于DOUBLE_CLICK_INTERVAL
                Logger.v("双击")
                isDoubleClick = true
            }
            firstClickTime = 0L
            secondClickTime = 0L
        }
    }

    fun onActionUp(event: MotionEvent) {
        // 防止下拉的时候双手缩放
        if (event.pointerCount == 1) {
            if (mConfig.curCanvasTranslationY > mConfig.MAX_CANVAS_TRANSLATION_Y) {
                exit()
            } else {
                restore()
            }
        }
        isUp = true
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