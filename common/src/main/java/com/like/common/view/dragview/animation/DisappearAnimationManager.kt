package com.like.common.view.dragview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * Activity消失的动画
 */
class DisappearAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view, info) {
    private var pendingScaleX = info.originWidth / view.width.toFloat()
    private var pendingScaleY = info.originHeight / view.height.toFloat()
    private var pendingLeft = info.originLeft
    private var pendingTop = info.originTop

    fun setCurData(info: DragInfo): DisappearAnimationManager {
        // 根据DragInfo重新计算数据，因为有ViewPager的影响
        pendingScaleX = info.originWidth / view.width.toFloat()
        pendingScaleY = info.originHeight / view.height.toFloat()
        pendingLeft = info.originLeft
        pendingTop = info.originTop
        return this
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        view.pivotX = 0f
        view.pivotY = 0f
        animatorSet.play(ObjectAnimator.ofFloat(view, "x", 0f, pendingLeft))
                .with(ObjectAnimator.ofFloat(view, "y", 0f, pendingTop))
                .with(ObjectAnimator.ofFloat(view, "scaleX", 1f, pendingScaleX))
                .with(ObjectAnimator.ofFloat(view, "scaleY", 1f, pendingScaleY))
    }

    override fun onEnd() {
        finishActivity()
    }

}