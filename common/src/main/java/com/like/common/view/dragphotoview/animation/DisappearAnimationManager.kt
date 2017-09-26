package com.like.common.view.dragphotoview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * DragPhotoViewActivity消失的动画
 */
class DisappearAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    private val initTranslationX = dragPhotoView.width.toFloat() * dragPhotoViewInfo.index// 要考虑ViewPager切换页面对初始值的影响
    private val pendingScaleX = dragPhotoViewInfo.originWidth / dragPhotoView.width.toFloat()
    private val pendingScaleY = dragPhotoViewInfo.originHeight / dragPhotoView.height.toFloat()

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        dragPhotoView.pivotX = 0f
        dragPhotoView.pivotY = 0f
        animatorSet.play(ObjectAnimator.ofFloat(dragPhotoView, "x", initTranslationX, initTranslationX + dragPhotoViewInfo.originLeft.toFloat()))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "y", 0f, dragPhotoViewInfo.originTop.toFloat()))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleX", 1f, pendingScaleX))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleY", 1f, pendingScaleY))
    }

    override fun onEnd() {
        val activity = dragPhotoView.context
        if (activity is Activity) {
            activity.finish()
            activity.overridePendingTransition(0, 0)
        }
    }

}