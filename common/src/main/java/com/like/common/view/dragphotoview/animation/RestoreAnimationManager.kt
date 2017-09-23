package com.like.common.view.dragphotoview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo
import com.like.logger.Logger

/**
 * 在DragPhotoViewActivity中由缩放到还原的动画管理
 */
class RestoreAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    var alpha: Int = 255
    var translateX: Float = 0f
    var translateY: Float = 0f
    var scale: Float = 1f
    var minScale: Float = 0.5f
    var isStart: Boolean = false

    init {
        minScale = dragPhotoViewInfo.originWidth.toFloat() / dragPhotoView.mWidth
    }

    override fun start() {
        if (!isStart) {
            isStart = true
            animatorSet.play(ValueAnimator.ofFloat(scale, 1f).apply {
                duration = AnimationManager.DURATION
                addUpdateListener {
                    scale = it.animatedValue as Float
                    dragPhotoView.invalidate()
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        animation?.removeAllListeners()
                    }
                })
            })
                    .with(ValueAnimator.ofFloat(translateX, 0f).apply {
                        duration = AnimationManager.DURATION
                        addUpdateListener {
                            translateX = it.animatedValue as Float
                        }
                    })
                    .with(ValueAnimator.ofFloat(translateY, 0f).apply {
                        duration = AnimationManager.DURATION
                        addUpdateListener {
                            translateY = it.animatedValue as Float
                        }
                    })
                    .with(ValueAnimator.ofInt(alpha, 255).apply {
                        duration = AnimationManager.DURATION
                        addUpdateListener {
                            alpha = it.animatedValue as Int
                        }
                    })
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    isStart = false
                }
            })
            Logger.d("startRestoreAnimator scale = $scale alpha = $alpha translateX = $translateX translateY = $translateY")
            animatorSet.start()
        }
    }

    override fun finish() {
        translateX = -dragPhotoView.width / 2 + dragPhotoView.width * scale / 2
        translateY = -dragPhotoView.height / 2 + dragPhotoView.height * scale / 2
        Logger.e("finish translateX = $translateX translateY = $translateY")
        dragPhotoView.invalidate()
    }

    fun updateTranslateX(translateX: Float) {
        this.translateX = translateX
    }

    fun updateTranslateY(translateY: Float) {
        this.translateY = if (translateY < 0) 0f else translateY
    }

    fun updateScale() {
        val translateYPercent = translateY / AnimationManager.MAX_RESTORE_ANIMATOR_TRANSLATE_Y
        val scale = 1 - translateYPercent
        this.scale = when {
            scale < minScale -> minScale
            scale > 1f -> 1f
            else -> scale
        }
    }

    fun updateAlpha() {
        val translateYPercent = translateY / AnimationManager.MAX_RESTORE_ANIMATOR_TRANSLATE_Y
        val alpha = (255 * (1 - translateYPercent)).toInt()
        this.alpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
    }
}
