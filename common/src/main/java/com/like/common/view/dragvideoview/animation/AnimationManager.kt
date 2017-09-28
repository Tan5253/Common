package com.like.common.view.dragvideoview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import com.like.common.view.dragvideoview.DragVideoView
import com.like.common.view.dragvideoview.DragVideoViewInfo

abstract class AnimationManager(val dragVideoView: DragVideoView, val dragVideoViewInfo: DragVideoViewInfo) {
    companion object {
        const val DURATION = 200L
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