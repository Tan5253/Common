package com.like.common.dragVideoView.dragvideodragVideoView.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import com.like.common.view.dragvideoview.DragVideoView
import com.like.common.view.dragvideoview.DragVideoViewInfo
import com.like.common.view.dragvideoview.animation.AnimationManager

/**
 * DragPhotoViewActivity消失的动画
 */
class DisappearAnimationManager(dragVideoView: DragVideoView, dragVideoViewInfo: DragVideoViewInfo) : AnimationManager(dragVideoView, dragVideoViewInfo) {
    private val pendingScaleX = dragVideoViewInfo.originWidth / dragVideoView.width.toFloat()
    private val pendingScaleY = dragVideoViewInfo.originHeight / dragVideoView.height.toFloat()

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        dragVideoView.pivotX = 0f
        dragVideoView.pivotY = 0f
        animatorSet.play(ObjectAnimator.ofFloat(dragVideoView, "x", 0f, dragVideoViewInfo.originLeft.toFloat()))
                .with(ObjectAnimator.ofFloat(dragVideoView, "y", 0f, dragVideoViewInfo.originTop.toFloat()))
                .with(ObjectAnimator.ofFloat(dragVideoView, "scaleX", 1f, pendingScaleX))
                .with(ObjectAnimator.ofFloat(dragVideoView, "scaleY", 1f, pendingScaleY))
    }

    override fun onEnd() {
        val activity = dragVideoView.context
        if (activity is Activity) {
            activity.finish()
            activity.overridePendingTransition(0, 0)
        }
    }

}