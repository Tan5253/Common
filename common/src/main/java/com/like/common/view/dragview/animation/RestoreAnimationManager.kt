package com.like.common.view.dragview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * 在Activity中，view从缩放状态还原的动画管理
 */
class RestoreAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view) {
    val MAX_CANVAS_TRANSLATION_Y = view.height.toFloat() / 4
    var minCanvasScale = info.originWidth / view.width

    fun setCurData(info: DragInfo) {
        // 根据DragInfo重新计算数据，因为有ViewPager的影响
        view.mAnimationConfig.init()
        minCanvasScale = info.originWidth / view.width
    }

    override
    fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ValueAnimator.ofFloat(view.mAnimationConfig.canvasScale, 1f).apply {
            addUpdateListener {
                view.mAnimationConfig.canvasScale = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(view.mAnimationConfig.canvasTranslationX, 0f).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(view.mAnimationConfig.canvasTranslationY, 0f).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationY = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofInt(view.mAnimationConfig.canvasBgAlpha, 255).apply {
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

    fun updateCanvasTranslationX(translationX: Float) {
        view.mAnimationConfig.canvasTranslationX = translationX
    }

    fun updateCanvasTranslationY(translationY: Float) {
        view.mAnimationConfig.canvasTranslationY = translationY
    }

    fun updateCanvasScale() {
        val translateYPercent = Math.abs(view.mAnimationConfig.canvasTranslationY) / view.height
        val scale = 1 - translateYPercent
        view.mAnimationConfig.canvasScale = when {
            scale < minCanvasScale -> minCanvasScale
            scale > 1f -> 1f
            else -> scale
        }
    }

    fun updateCanvasBgAlpha() {
        val translateYPercent = Math.abs(view.mAnimationConfig.canvasTranslationY) / view.height
        val alpha = (255 * (1 - translateYPercent)).toInt()
        view.mAnimationConfig.canvasBgAlpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
    }
}