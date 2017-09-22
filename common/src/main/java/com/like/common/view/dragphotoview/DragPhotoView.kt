package com.like.common.view.dragphotoview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import com.github.chrisbanes.photoview.PhotoView
import com.like.common.util.PhoneUtils

class DragPhotoView : PhotoView {
    val mPaint: Paint = Paint().apply { color = Color.BLACK }

    var mDownX: Float = 0f
    var mDownY: Float = 0f

    val screenWidth = PhoneUtils.getInstance(context).mPhoneStatus.screenWidth.toFloat()
    val screenHeight = PhoneUtils.getInstance(context).mPhoneStatus.screenHeight.toFloat()
    var mWidth: Float = 0f
    var mHeight: Float = 0f

    var canFinish: Boolean = false

    var mTapListener: OnTapListener? = null
    var mExitListener: OnExitListener? = null

    val mAnimationManager: AnimationManager = AnimationManager(this)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        mPaint.alpha = mAnimationManager.mAlpha
        canvas?.drawRect(0f, 0f, screenWidth, screenHeight, mPaint)
        canvas?.translate(mAnimationManager.mTranslateX, mAnimationManager.mTranslateY)
        canvas?.scale(mAnimationManager.mScale, mAnimationManager.mScale, mWidth / 2, mHeight / 2)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // 当scale == 1时才能drag
        if (scale == 1f) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    onEventDown(event)
                    canFinish = !canFinish
                }
                MotionEvent.ACTION_MOVE -> {
                    // ViewPager的事件
                    if (mAnimationManager.mTranslateY == 0f && mAnimationManager.mTranslateX != 0f) {
                        mAnimationManager.mScale = 1f
                        return super.dispatchTouchEvent(event)
                    }

                    // 单手指按下，并在Y方向上拖动了一段距离
                    if (mAnimationManager.mTranslateY >= 0 && event.pointerCount == 1) {
                        onEventMove(event)
                        return true
                    }

                    // 防止下拉的时候双手缩放
                    if (mAnimationManager.mTranslateY >= 0 && mAnimationManager.mScale < 0.95f) {
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // 防止下拉的时候双手缩放
                    if (event.pointerCount == 1) {
                        onEventUp(event)
                        // 延时判断是否可以退出
                        postDelayed({
                            if (mAnimationManager.mTranslateX == 0f && mAnimationManager.mTranslateY == 0f && canFinish) {
                                mTapListener?.onTap(this@DragPhotoView)
                            }
                            canFinish = false
                        }, 300)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun onEventDown(event: MotionEvent) {
        mDownX = event.x
        mDownY = event.y
    }

    private fun onEventMove(event: MotionEvent) {
        mAnimationManager.updateTranslateX(event.x - mDownX)
                .updateTranslateY(event.y - mDownY)
                .updateScale()
                .updateAlpha()
        invalidate()
    }

    private fun onEventUp(event: MotionEvent) {
        if (mAnimationManager.mTranslateY > AnimationManager.MAX_TRANSLATE_Y) {
            mExitListener?.onExit(this, mAnimationManager.mTranslateX, mAnimationManager.mTranslateY, mWidth, mHeight)
        } else {
            mAnimationManager.start()
        }
    }

}