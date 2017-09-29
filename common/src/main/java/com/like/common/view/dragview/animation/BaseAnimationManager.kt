package com.like.common.view.dragview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.app.Activity
import com.like.common.view.dragview.view.BaseDragView

abstract class BaseAnimationManager(val view: BaseDragView) {
    companion object {
        const val DURATION = 3000L
    }

    private var isStart: Boolean = false
    private val animatorSet: AnimatorSet = AnimatorSet().apply {
        duration = BaseAnimationManager.DURATION
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                isStart = true
                onStart()
                super.onAnimationStart(animation)
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

    fun finishActivity() {
        val activity = view.context
        if (activity is Activity) {
            activity.finish()
            activity.overridePendingTransition(0, 0)
        }
    }

    abstract fun fillAnimatorSet(animatorSet: AnimatorSet)
    open fun onStart() {}
    open fun onEnd() {}

}