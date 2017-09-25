package com.like.common.view.dragphotoview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import com.github.chrisbanes.photoview.PhotoView
import com.like.common.util.PhoneUtils
import com.like.common.view.dragphotoview.animation.*

class DragPhotoView(context: Context, dragPhotoViewInfo: DragPhotoViewInfo) : PhotoView(context) {
    private val mPaint: Paint = Paint().apply { color = Color.BLACK }

    private var mDownX: Float = 0f
    private var mDownY: Float = 0f

    private val screenWidth = PhoneUtils.getInstance(context).mPhoneStatus.screenWidth.toFloat()
    private val screenHeight = PhoneUtils.getInstance(context).mPhoneStatus.screenHeight.toFloat()
    private var mWidth: Float = 0f
    private var mHeight: Float = 0f

    private var canFinish: Boolean = false

    internal val mRestoreAnimationManager: RestoreAnimationManager by lazy { RestoreAnimationManager(this, dragPhotoViewInfo) }
    private val mEnterAnimationManager: EnterAnimationManager by lazy { EnterAnimationManager(this, dragPhotoViewInfo) }
    private val mExitAnimationManager: ExitAnimationManager by lazy { ExitAnimationManager(this, dragPhotoViewInfo) }
    private val mDisappearAnimationManager: DisappearAnimationManager by lazy { DisappearAnimationManager(this, dragPhotoViewInfo) }

    // 以下代码：处理ViewPager由于滑动冲突导致的不能在每次滚动完毕时正常回归原位的bug
    private var scrollState = 0

    fun init() {
        val viewPager = getParentViewPager()
        viewPager?.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
//                Logger.e("onPageSelected position = $position")
                restore()
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (scrollState == 1 && state == 0) {
                    restore()
                }
                scrollState = state
//                Logger.d("onPageScrollStateChanged state = $state")
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                Logger.w("onPageScrolled position = $position")
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

        })
    }
    // 以上代码：处理ViewPager由于滑动冲突导致的不能在每次滚动完毕时正常回归原位的bug

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
        init()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // 当scale == 1时才能drag
        if (scale == 1f) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownX = event.x
                    mDownY = event.y
                    canFinish = !canFinish
                }
                MotionEvent.ACTION_MOVE -> {
                    // ViewPager的事件
                    if (mRestoreAnimationManager.canvasTranslationY == 0f && mRestoreAnimationManager.canvasTranslationX != 0f) {
                        return super.dispatchTouchEvent(event)
                    }

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
                        if (mRestoreAnimationManager.canvasTranslationY > AnimationManager.MAX_RESTORE_ANIMATOR_TRANSLATE_Y) {
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

    private fun getParentViewPager(): ViewPager? {
        return if (parent != null && parent is ViewPager) {
            parent as ViewPager
        } else
            null
    }

}