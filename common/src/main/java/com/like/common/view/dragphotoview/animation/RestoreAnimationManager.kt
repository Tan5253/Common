package com.like.common.view.dragphotoview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.util.TypedValue
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo
import com.like.logger.Logger

/**
 * 从缩放状态在DragPhotoViewActivity中还原的动画管理
 */
class RestoreAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    var canvasBgAlpha: Int = 255
    var canvasTranslationX: Float = 0f
    var canvasTranslationY: Float = 0f
    var canvasScale: Float = 1f
    private val MIN_CANVAS_SCALE: Float = dragPhotoViewInfo.originWidth.toFloat() / dragPhotoView.width
    val MAX_CANVAS_TRANSLATION_Y: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, dragPhotoView.resources.displayMetrics)

    override
    fun fillAnimatorSet(animatorSet: AnimatorSet) {
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
        Logger.e("canvasTranslationY = $canvasTranslationY MAX_CANVAS_TRANSLATION_Y = $MAX_CANVAS_TRANSLATION_Y")
        val translateYPercent = canvasTranslationY / MAX_CANVAS_TRANSLATION_Y
        val scale = 1 - translateYPercent
        canvasScale = when {
            scale < MIN_CANVAS_SCALE -> MIN_CANVAS_SCALE
            scale > 1f -> 1f
            else -> scale
        }
    }

    fun updateCanvasBgAlpha() {
        Logger.e("canvasTranslationY = $canvasTranslationY MAX_CANVAS_TRANSLATION_Y = $MAX_CANVAS_TRANSLATION_Y")
        val translateYPercent = canvasTranslationY / MAX_CANVAS_TRANSLATION_Y
        val alpha = (255 * (1 - translateYPercent)).toInt()
        canvasBgAlpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
    }
}
