package com.like.common.view.dragphotoview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.logger.Logger

class AnimationManager(val view: DragPhotoView) {
    companion object {
        const val DURATION = 300L
        const val MAX_RESTORE_ANIMATOR_TRANSLATE_Y = 500
    }

    // 在DragPhotoViewActivity中由缩放到还原的动画
    private val restoreAnimatorSet: AnimatorSet = AnimatorSet()
    var restoreAnimAlpha: Int = 255
    var restoreAnimTranslateX: Float = 0f
    var restoreAnimTranslateY: Float = 0f
    var restoreAnimScale: Float = 1f
    var restoreAnimMinScale: Float = 0.5f
    var restoreAnimIsStart: Boolean = false
    // 进入退出DragPhotoViewActivity的动画


    fun updateRestoreAnimTranslateX(translateX: Float): AnimationManager {
        restoreAnimTranslateX = translateX
        return this
    }

    fun updateRestoreAnimTranslateY(translateY: Float): AnimationManager {
        restoreAnimTranslateY = if (translateY < 0) 0f else translateY
        return this
    }

    fun updateRestoreAnimScale(): AnimationManager {
        val translateYPercent = restoreAnimTranslateY / MAX_RESTORE_ANIMATOR_TRANSLATE_Y
        val scale = 1 - translateYPercent
        restoreAnimScale = when {
            scale < restoreAnimMinScale -> restoreAnimMinScale
            scale > 1f -> 1f
            else -> scale
        }
        return this
    }

    fun updateRestoreAnimAlpha(): AnimationManager {
        val translateYPercent = restoreAnimTranslateY / MAX_RESTORE_ANIMATOR_TRANSLATE_Y
        val alpha = (255 * (1 - translateYPercent)).toInt()
        restoreAnimAlpha = when {
            alpha > 255 -> 255
            alpha < 0 -> 0
            else -> alpha
        }
        return this
    }

    /**
     * 启动还原动画
     */
    fun startRestoreAnimtor() {
        if (!restoreAnimIsStart) {
            restoreAnimIsStart = true
            restoreAnimatorSet.play(ValueAnimator.ofFloat(restoreAnimScale, 1f).apply {
                duration = DURATION
                addUpdateListener {
                    restoreAnimScale = it.animatedValue as Float
                    view.invalidate()
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        animation?.removeAllListeners()
                    }
                })
            })
                    .with(ValueAnimator.ofFloat(restoreAnimTranslateX, 0f).apply {
                        duration = DURATION
                        addUpdateListener {
                            restoreAnimTranslateX = it.animatedValue as Float
                        }
                    })
                    .with(ValueAnimator.ofFloat(restoreAnimTranslateY, 0f).apply {
                        duration = DURATION
                        addUpdateListener {
                            restoreAnimTranslateY = it.animatedValue as Float
                        }
                    })
                    .with(ValueAnimator.ofInt(restoreAnimAlpha, 255).apply {
                        duration = DURATION
                        addUpdateListener {
                            restoreAnimAlpha = it.animatedValue as Int
                        }
                    })
            restoreAnimatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    restoreAnimIsStart = false
                }
            })
            Logger.d("startRestoreAnimtor restoreAnimScale = $restoreAnimScale restoreAnimAlpha = $restoreAnimAlpha restoreAnimTranslateX = $restoreAnimTranslateX restoreAnimTranslateY = $restoreAnimTranslateY")
            restoreAnimatorSet.start()
        }
    }

    fun finish() {
        restoreAnimTranslateX = -view.width / 2 + view.width * restoreAnimScale / 2
        restoreAnimTranslateY = -view.height / 2 + view.height * restoreAnimScale / 2
        view.invalidate()
    }

}
