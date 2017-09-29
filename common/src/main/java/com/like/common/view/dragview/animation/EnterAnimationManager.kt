package com.like.common.view.dragview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * 进入Activity的动画
 */
class EnterAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view, info) {
    private val initScaleX = info.originWidth / view.width
    private val initScaleY = info.originHeight / view.height
    private val initTranslationX = info.originCenterX - view.width.toFloat() / 2
    private val initTranslationY = info.originCenterY - view.height.toFloat() / 2

    init {
        // 移动到原始位置，并缩放到原始大小。开始动画前的准备工作
        view.scaleX = initScaleX
        view.scaleY = initScaleY

        view.translationX = initTranslationX
        view.translationY = initTranslationY
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        // 当进入动画后，放大了就会填满。所以不需要translation动画
        animatorSet.play(ObjectAnimator.ofFloat(view, "x", view.x, 0f))
                .with(ObjectAnimator.ofFloat(view, "y", view.y, 0f))
                .with(ObjectAnimator.ofFloat(view, "scaleX", initScaleX, 1f))
                .with(ObjectAnimator.ofFloat(view, "scaleY", initScaleY, 1f))
    }

}