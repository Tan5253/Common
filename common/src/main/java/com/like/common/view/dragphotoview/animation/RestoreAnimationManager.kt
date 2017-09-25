package com.like.common.view.dragphotoview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * 从缩放状态在DragPhotoViewActivity中还原的动画管理
 */
class RestoreAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    var canvasBgAlpha: Int = 255
    var canvasTranslationX: Float = 0f
    var canvasTranslationY: Float = 0f
    var canvasScale: Float = 1f
    private var minCanvasScale: Float = 0.5f

    init {
        minCanvasScale = dragPhotoViewInfo.originWidth.toFloat() / dragPhotoView.width
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ValueAnimator.ofFloat(canvasScale, 1f).apply {
            duration = AnimationManager.DURATION
            addUpdateListener {
                canvasScale = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(canvasTranslationX, 0f).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        canvasTranslationX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(canvasTranslationY, 0f).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        canvasTranslationY = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofInt(canvasBgAlpha, 255).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        canvasBgAlpha = it.animatedValue as Int
                        dragPhotoView.invalidate()
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
        canvasTranslationY = if (translationY < 0) 0f else translationY
    }

    fun updateCanvasScale() {
        val translateYPercent = canvasTranslationY / AnimationManager.MAX_RESTORE_ANIMATOR_TRANSLATE_Y
        val scale = 1 - translateYPercent
        canvasScale = when {
            scale < minCanvasScale -> minCanvasScale
            scale > 1f -> 1f
            else -> scale
        }
    }

    fun updateCanvasBgAlpha() {
        val translateYPercent = canvasTranslationY / AnimationManager.MAX_RESTORE_ANIMATOR_TRANSLATE_Y
        val alpha = (255 * (1 - translateYPercent)).toInt()
        canvasBgAlpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
    }
}
