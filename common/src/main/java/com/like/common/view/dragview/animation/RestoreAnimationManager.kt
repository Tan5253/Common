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
class RestoreAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view, info) {
    var canvasBgAlpha: Int = 255
    var canvasTranslationX: Float = 0f
    var canvasTranslationY: Float = 0f
    var canvasScale: Float = 1f
    val MIN_CANVAS_SCALE: Float = info.originWidth / view.width
    val MAX_CANVAS_TRANSLATION_Y: Float = view.height.toFloat() / 4

    override
    fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ValueAnimator.ofFloat(canvasScale, 1f).apply {
            duration = DURATION
            addUpdateListener {
                canvasScale = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(canvasTranslationX, 0f).apply {
                    duration = DURATION
                    addUpdateListener {
                        canvasTranslationX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(canvasTranslationY, 0f).apply {
                    duration = DURATION
                    addUpdateListener {
                        canvasTranslationY = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofInt(canvasBgAlpha, 255).apply {
                    duration = DURATION
                    addUpdateListener {
                        canvasBgAlpha = it.animatedValue as Int
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
        canvasTranslationX = translationX
    }

    fun updateCanvasTranslationY(translationY: Float) {
        canvasTranslationY = translationY
    }

    fun updateCanvasScale() {
        val translateYPercent = Math.abs(canvasTranslationY) / view.height
        val scale = 1 - translateYPercent
        canvasScale = when {
            scale < MIN_CANVAS_SCALE -> MIN_CANVAS_SCALE
            scale > 1f -> 1f
            else -> scale
        }
    }

    fun updateCanvasBgAlpha() {
        val translateYPercent = Math.abs(canvasTranslationY) / view.height
        val alpha = (255 * (1 - translateYPercent)).toInt()
        canvasBgAlpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
    }
}