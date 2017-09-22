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
    companion object {
        const val MAX_TRANSLATE_Y = 500
    }

    val mPaint: Paint = Paint().apply { color = Color.BLACK }

    var mDownX: Float = 0f
    var mDownY: Float = 0f

    val screenWidth = PhoneUtils.getInstance(context).mPhoneStatus.screenWidth.toFloat()
    val screenHeight = PhoneUtils.getInstance(context).mPhoneStatus.screenHeight.toFloat()
    var mWidth: Float = 0f
    var mHeight: Float = 0f

    var canFinish: Boolean = false
    var isMyTouchEvent: Boolean = false// 是否是PhotoView的touch事件

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
                        //如果不消费事件，则不作操作
                        if (!isMyTouchEvent) {
                            mAnimationManager.mScale = 1f
                            return super.dispatchTouchEvent(event)
                        }
                    }

                    // 单手指按下，并在Y方向上拖动了一段距离
                    if (mAnimationManager.mTranslateY >= 0 && event.pointerCount == 1) {
                        onEventMove(event)
                        //如果有上下位移 则不交给viewpager
                        if (mAnimationManager.mTranslateY != 0f) {
                            isMyTouchEvent = true
                        }
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
                        isMyTouchEvent = false
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

    fun onEventDown(event: MotionEvent) {
        mDownX = event.x
        mDownY = event.y
    }

    fun onEventMove(event: MotionEvent) {
        val moveY = event.y
        val moveX = event.x
        mAnimationManager.mTranslateX = moveX - mDownX
        mAnimationManager.mTranslateY = moveY - mDownY

        if (mAnimationManager.mTranslateY < 0) {
            mAnimationManager.mTranslateY = 0f
        }

        val translateYPercent = mAnimationManager.mTranslateY / MAX_TRANSLATE_Y
        mAnimationManager.mScale = 1 - translateYPercent
        if (mAnimationManager.mScale < mAnimationManager.mMinScale) {
            mAnimationManager.mScale = mAnimationManager.mMinScale
        } else if (mAnimationManager.mScale > 1f) {
            mAnimationManager.mScale = 1f
        }

        mAnimationManager.mAlpha = (255 * (1 - translateYPercent)).toInt()
        if (mAnimationManager.mAlpha > 255) {
            mAnimationManager.mAlpha = 255
        } else if (mAnimationManager.mAlpha < 0) {
            mAnimationManager.mAlpha = 0
        }

        invalidate()
    }

    fun onEventUp(event: MotionEvent) {
        if (mAnimationManager.mTranslateY > MAX_TRANSLATE_Y) {
            mExitListener?.onExit(this, mAnimationManager.mTranslateX, mAnimationManager.mTranslateY, mWidth.toFloat(), mHeight.toFloat())
        } else {
            mAnimationManager.start()
        }
    }

}