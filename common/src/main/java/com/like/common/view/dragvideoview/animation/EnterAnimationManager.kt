package com.like.common.view.dragvideoview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragvideoview.DragVideoView
import com.like.common.view.dragvideoview.DragVideoViewInfo


/**
 * 进入DragVideoViewActivity的动画
 */
class EnterAnimationManager(dragVideoView: DragVideoView, dragVideoViewInfo: DragVideoViewInfo) : AnimationManager(dragVideoView, dragVideoViewInfo) {
    private val initScaleX = dragVideoViewInfo.originWidth.toFloat() / dragVideoView.width
    private val initScaleY = dragVideoViewInfo.originHeight.toFloat() / dragVideoView.height
    private val initTranslationX = dragVideoViewInfo.originCenterX - dragVideoView.width.toFloat() / 2
    private val initTranslationY = dragVideoViewInfo.originCenterY - dragVideoView.height.toFloat() / 2

    init {
        // 移动到原始位置，并缩放到原始大小。开始动画前的准备工作
        dragVideoView.scaleX = initScaleX
        dragVideoView.scaleY = initScaleY

        dragVideoView.translationX = initTranslationX
        dragVideoView.translationY = initTranslationY
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        // 当进入动画后，放大了就会填满。所以不需要translation动画
        animatorSet.play(ObjectAnimator.ofFloat(dragVideoView, "x", dragVideoView.x, 0f))
                .with(ObjectAnimator.ofFloat(dragVideoView, "y", dragVideoView.y, 0f))
                .with(ObjectAnimator.ofFloat(dragVideoView, "scaleX", initScaleX, 1f))
                .with(ObjectAnimator.ofFloat(dragVideoView, "scaleY", initScaleY, 1f))
    }

}