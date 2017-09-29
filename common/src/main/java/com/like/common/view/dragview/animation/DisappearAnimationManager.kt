package com.like.common.view.dragview.animation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * Activity消失的动画
 */
class DisappearAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view) {
    private var pendingScaleX = info.originWidth / view.width.toFloat()
    private var pendingScaleY = info.originHeight / view.height.toFloat()
    private var pendingTranslationX = info.originCenterX - view.width / 2
    private var pendingTranslationY = info.originCenterY - view.height / 2

    fun setCurData(info: DragInfo) {
        // 根据DragInfo重新计算数据，因为有ViewPager的影响
        pendingScaleX = info.originWidth / view.width.toFloat()
        pendingScaleY = info.originHeight / view.height.toFloat()
        pendingTranslationX = info.originCenterX - view.width / 2
        pendingTranslationY = info.originCenterY - view.height / 2
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ValueAnimator.ofFloat(1f, pendingScaleX).apply {
            addUpdateListener {
                view.mAnimationConfig.canvasScale = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(0f, pendingTranslationX).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(0f, pendingTranslationY).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationY = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofInt(255, 0).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasBgAlpha = it.animatedValue as Int
                        view.invalidate()
                    }
                })
    }

    override fun onEnd() {
        finishActivity()
    }

}