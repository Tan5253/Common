package com.like.common.view.dragphotoview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.logger.Logger

class AnimationManager(val view: DragPhotoView) {
    companion object {
        const val MAX_TRANSLATE_Y = 500
        const val DURATION = 300L
    }

    private val animatorSet: AnimatorSet = AnimatorSet()
    var mAlpha: Int = 255
    var mTranslateX: Float = 0f
    var mTranslateY: Float = 0f
    var mScale: Float = 1f
    var mMinScale: Float = 0.5f
    var isStart: Boolean = false

    fun updateTranslateX(translateX: Float): AnimationManager {
        mTranslateX = translateX
        return this
    }

    fun updateTranslateY(translateY: Float): AnimationManager {
        mTranslateY = if (translateY < 0) 0f else translateY
        return this
    }

    fun updateScale(): AnimationManager {
        val translateYPercent = mTranslateY / MAX_TRANSLATE_Y
        val scale = 1 - translateYPercent
        mScale = when {
            scale < mMinScale -> mMinScale
            scale > 1f -> 1f
            else -> scale
        }
        return this
    }

    fun updateAlpha(): AnimationManager {
        val translateYPercent = mTranslateY / MAX_TRANSLATE_Y
        val alpha = (255 * (1 - translateYPercent)).toInt()
        mAlpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
        return this
    }

    fun restoreImmediately() {
        mAlpha = 255
        mTranslateX = 0f
        mTranslateY = 0f
        mScale = 1f
        isStart = false
        view.invalidate()
    }

    /**
     * 启动还原动画
     */
    fun restoreSmooth() {
        if (!isStart) {
            isStart = true
            animatorSet.play(getScaleAnimation()).with(getTranslateXAnimation()).with(getTranslateYAnimation()).with(getAlphaAnimation())
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    isStart = false
                }
            })
            Logger.d("restoreSmooth mScale = $mScale mAlpha = $mAlpha mTranslateX = $mTranslateX mTranslateY = $mTranslateY")
            animatorSet.start()
        }
    }

    fun finish() {
        mTranslateX = -view.width / 2 + view.width * mScale / 2
        mTranslateY = -view.height / 2 + view.height * mScale / 2
        view.invalidate()
    }

    private fun getAlphaAnimation() = ValueAnimator.ofInt(mAlpha, 255).apply {
        duration = DURATION
        addUpdateListener {
            mAlpha = it.animatedValue as Int
        }
    }

    private fun getTranslateXAnimation() = ValueAnimator.ofFloat(mTranslateX, 0f).apply {
        duration = DURATION
        addUpdateListener {
            mTranslateX = it.animatedValue as Float
        }
    }

    private fun getTranslateYAnimation() = ValueAnimator.ofFloat(mTranslateY, 0f).apply {
        duration = DURATION
        addUpdateListener {
            mTranslateY = it.animatedValue as Float
        }
    }

    private fun getScaleAnimation() = ValueAnimator.ofFloat(mScale, 1f).apply {
        duration = DURATION
        addUpdateListener {
            mScale = it.animatedValue as Float
            view.invalidate()
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                animation?.removeAllListeners()
            }
        })
    }

}
