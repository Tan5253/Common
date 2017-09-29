package com.like.common.view.dragview.animation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * 从缩放状态到退出Activity的动画
 */
class ExitAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view) {
    // 注意：这里是以缩放后的视图为初始情况
    private var pendingScaleX = info.originWidth / view.width.toFloat()
    private var pendingScaleY = info.originHeight / view.height.toFloat()
    private var pendingTranslationX = info.originCenterX - view.width.toFloat() / 2
    private var pendingTranslationY = info.originCenterY - view.height.toFloat() / 2

    fun setCurData(info: DragInfo) {
        // 根据DragInfo重新计算数据，因为有ViewPager的影响
        pendingScaleX = info.originWidth / view.width.toFloat()
        pendingScaleY = info.originHeight / view.height.toFloat()
        pendingTranslationX = info.originCenterX - view.width.toFloat() / 2
        pendingTranslationY = info.originCenterY - view.height.toFloat() / 2
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ValueAnimator.ofFloat(view.mAnimationConfig.canvasScale, pendingScaleX).apply {
            addUpdateListener {
                view.mAnimationConfig.canvasScale = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(view.mAnimationConfig.canvasTranslationX, pendingTranslationX).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(view.mAnimationConfig.canvasTranslationY, pendingTranslationY).apply {
                    addUpdateListener {
                        view.mAnimationConfig.canvasTranslationY = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofInt(view.mAnimationConfig.canvasBgAlpha, 0).apply {
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