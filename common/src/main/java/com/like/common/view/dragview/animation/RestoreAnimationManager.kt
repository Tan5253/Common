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
    var canvasBgAlpha = 255
    var canvasTranslationX = 0f
    var canvasTranslationY = 0f
    var canvasScale = 1f
    var minCanvasScale = info.originWidth / view.width
    val MAX_CANVAS_TRANSLATION_Y = view.height.toFloat() / 4

    fun setCurData(info: DragInfo): RestoreAnimationManager {
        // 根据DragInfo重新计算数据，因为有ViewPager的影响
        canvasBgAlpha = 255
        canvasTranslationX = 0f
        canvasTranslationY = 0f
        canvasScale = 1f
        minCanvasScale = info.originWidth / view.width
        return this
    }

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
            scale < minCanvasScale -> minCanvasScale
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