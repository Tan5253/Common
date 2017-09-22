package com.like.common.view.dragphotoview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator

class AnimationManager(val view: DragPhotoView) {
    companion object {
        const val DURATION = 300L
    }

    var mAlpha: Int = 255
    var mTranslateX: Float = 0f
    var mTranslateY: Float = 0f
    var mScale: Float = 1f
    var mMinScale: Float = 0.5f

    fun start() {
        getScaleAnimation().start()
        getTranslateXAnimation().start()
        getTranslateYAnimation().start()
        getAlphaAnimation().start()
    }

    fun finish() {
        mTranslateX = -view.width / 2 + view.width * mScale / 2
        mTranslateY = -view.height / 2 + view.height * mScale / 2
        view.invalidate()
    }

    fun getAlphaAnimation() = ValueAnimator.ofInt(mAlpha, 255).apply {
        duration = DURATION
        addUpdateListener {
            mAlpha = it.animatedValue as Int
        }
    }

    fun getTranslateXAnimation() = ValueAnimator.ofFloat(mTranslateX, 0f).apply {
        duration = DURATION
        addUpdateListener {
            mTranslateX = it.animatedValue as Float
        }
    }

    fun getTranslateYAnimation() = ValueAnimator.ofFloat(mTranslateY, 0f).apply {
        duration = DURATION
        addUpdateListener {
            mTranslateY = it.animatedValue as Float
        }
    }

    fun getScaleAnimation() = ValueAnimator.ofFloat(mScale, 1f).apply {
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
