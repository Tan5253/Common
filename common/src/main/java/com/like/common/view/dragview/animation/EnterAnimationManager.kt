package com.like.common.view.dragview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * 进入Activity的动画
 */
class EnterAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view, info) {
    private val initScaleX = info.originWidth / view.width
    private val initScaleY = info.originHeight / view.height
    private val initTranslationX = info.originCenterX - view.width.toFloat() / 2
    private val initTranslationY = info.originCenterY - view.height.toFloat() / 2

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        // 当进入动画后，放大了就会填满。所以不需要translation动画
        animatorSet.play(ValueAnimator.ofFloat(initScaleX, 1f).apply {
            duration = DURATION
            addUpdateListener {
                view.mAnimationConfig.canvasScale = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(initTranslationX, 0f).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(initTranslationY, 0f).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationY = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofInt(0, 255).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasBgAlpha = it.animatedValue as Int
                        view.invalidate()
                    }
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            animation?.removeAllListeners()
                        }
                    })
                })
    }

}