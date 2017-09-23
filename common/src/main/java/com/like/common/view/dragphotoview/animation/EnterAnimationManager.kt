package com.like.common.view.dragphotoview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * 进入DragPhotoViewActivity的动画
 */
class EnterAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    init {
        dragPhotoView.scaleX = dragPhotoViewInfo.originWidth.toFloat() / dragPhotoView.width
        dragPhotoView.scaleY = dragPhotoViewInfo.originHeight.toFloat() / dragPhotoView.height

        dragPhotoView.translationX = dragPhotoViewInfo.originCenterX - dragPhotoView.width.toFloat() / 2
        dragPhotoView.translationY = dragPhotoViewInfo.originCenterY - dragPhotoView.height.toFloat() / 2
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ObjectAnimator.ofFloat(dragPhotoView, "x", dragPhotoView.x, 0f))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "y", dragPhotoView.y, 0f))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleX", dragPhotoView.scaleX, 1f))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleY", dragPhotoView.scaleY, 1f))
    }

}
