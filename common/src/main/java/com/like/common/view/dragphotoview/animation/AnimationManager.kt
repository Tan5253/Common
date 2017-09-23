package com.like.common.view.dragphotoview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

abstract class AnimationManager(val dragPhotoView: DragPhotoView, val dragPhotoViewInfo: DragPhotoViewInfo) {
    companion object {
        const val DURATION = 300L
        const val MAX_RESTORE_ANIMATOR_TRANSLATE_Y = 500
    }

    private var isStart: Boolean = false
    private val animatorSet: AnimatorSet = AnimatorSet().apply {
        duration = AnimationManager.DURATION
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                isStart = true
                onStart()
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                animation?.removeAllListeners()
                isStart = false
                onEnd()
            }
        })
    }

    @Synchronized
    fun start() {
        if (isStart) {
            return
        }
        fillAnimatorSet(animatorSet)
        animatorSet.start()
    }

    fun cancel() {
        animatorSet.cancel()
    }

    abstract fun fillAnimatorSet(animatorSet: AnimatorSet)
    open fun onStart() {}
    open fun onEnd() {}
}
