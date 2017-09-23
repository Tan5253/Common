package com.like.common.view.dragphotoview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * 进入DragPhotoViewActivity的动画
 */
class EnterAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    private val initScaleX = dragPhotoViewInfo.originWidth.toFloat() / dragPhotoView.width
    private val initScaleY = dragPhotoViewInfo.originHeight.toFloat() / dragPhotoView.height
    private val initTranslationX = dragPhotoViewInfo.originCenterX - dragPhotoView.width.toFloat() / 2
    private val initTranslationY = dragPhotoViewInfo.originCenterY - dragPhotoView.height.toFloat() / 2

    init {
        dragPhotoView.scaleX = initScaleX
        dragPhotoView.scaleY = initScaleY

        dragPhotoView.translationX = initTranslationX
        dragPhotoView.translationY = initTranslationY
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ObjectAnimator.ofFloat(dragPhotoView, "x", dragPhotoView.x, 0f))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "y", dragPhotoView.y, 0f))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleX", initScaleX, 1f))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleY", initScaleY, 1f))
    }

}
