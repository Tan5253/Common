package com.like.common.view.dragphotoview.animation

import android.animation.ValueAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * 进入DragPhotoViewActivity的动画
 */
class EnterAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    init {
        dragPhotoView.scaleX = dragPhotoViewInfo.originWidth.toFloat() / dragPhotoView.mWidth
        dragPhotoView.scaleY = dragPhotoViewInfo.originHeight.toFloat() / dragPhotoView.mHeight

        dragPhotoView.translationX = dragPhotoViewInfo.originCenterX - dragPhotoView.mWidth / 2
        dragPhotoView.translationY = dragPhotoViewInfo.originCenterY - dragPhotoView.mHeight / 2
    }

    override fun start() {
        animatorSet.play(ValueAnimator.ofFloat(dragPhotoView.x, 0f).apply {
            duration = AnimationManager.DURATION
            addUpdateListener {
                dragPhotoView.x = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(dragPhotoView.y, 0f).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.y = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(dragPhotoView.scaleX, 1f).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.scaleX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(dragPhotoView.scaleY, 1f).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.scaleY = it.animatedValue as Float
                    }
                })
        animatorSet.start()
    }

    override fun finish() {

    }

}
