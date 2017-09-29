package com.like.common.view.dragview.animation

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
        animatorSet.play(ValueAnimator.ofFloat(view.mAnimationConfig.curCanvasScale, 1f).apply {
            addUpdateListener {
                view.mAnimationConfig.curCanvasScale = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(view.mAnimationConfig.curCanvasTranslationX, 0f).apply {
                    addUpdateListener {
                        view.mAnimationConfig.curCanvasTranslationX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(view.mAnimationConfig.curCanvasTranslationY, 0f).apply {
                    addUpdateListener {
                        view.mAnimationConfig.curCanvasTranslationY = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofInt(view.mAnimationConfig.curCanvasBgAlpha, 255).apply {
                    addUpdateListener {
                        view.mAnimationConfig.curCanvasBgAlpha = it.animatedValue as Int
                        view.invalidate()
                    }
                })
    }

    fun updateCanvasTranslationX(translationX: Float) {
        view.mAnimationConfig.curCanvasTranslationX = translationX
    }

    fun updateCanvasTranslationY(translationY: Float) {
        view.mAnimationConfig.curCanvasTranslationY = translationY
    }

    fun updateCanvasScale() {
        val translateYPercent = Math.abs(view.mAnimationConfig.curCanvasTranslationY) / view.height
        val scale = 1 - translateYPercent
        view.mAnimationConfig.curCanvasScale = when {
            scale < minCanvasScale -> minCanvasScale
            scale > 1f -> 1f
            else -> scale
        }
    }

    fun updateCanvasBgAlpha() {
        val translateYPercent = Math.abs(view.mAnimationConfig.curCanvasTranslationY) / view.height
        val alpha = (255 * (1 - translateYPercent)).toInt()
        view.mAnimationConfig.curCanvasBgAlpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
    }
}