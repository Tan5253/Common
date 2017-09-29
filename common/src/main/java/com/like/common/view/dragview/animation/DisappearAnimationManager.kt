package com.like.common.view.dragview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * Activity消失的动画
 */
class DisappearAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view, info) {
    private val pendingScaleX = info.originWidth / view.width.toFloat()
    private val pendingScaleY = info.originHeight / view.height.toFloat()

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        view.pivotX = 0f
        view.pivotY = 0f
        animatorSet.play(ObjectAnimator.ofFloat(view, "x", 0f, info.originLeft))
                .with(ObjectAnimator.ofFloat(view, "y", 0f, info.originTop))
                .with(ObjectAnimator.ofFloat(view, "scaleX", 1f, pendingScaleX))
                .with(ObjectAnimator.ofFloat(view, "scaleY", 1f, pendingScaleY))
    }

    override fun onEnd() {
        finishActivity()
    }

}