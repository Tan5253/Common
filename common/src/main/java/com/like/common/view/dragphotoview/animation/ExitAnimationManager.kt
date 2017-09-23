package com.like.common.view.dragphotoview.animation

import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

class ExitAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo, translationX: Float, translationY: Float) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    init {
//        dragPhotoView.translationX = -dragPhotoView.width / 2 + dragPhotoView.width * dragPhotoView.scaleX / 2
//        dragPhotoView.translationY = -dragPhotoView.height / 2 + dragPhotoView.height * dragPhotoView.scaleY / 2
//
//        val viewX = mTargetWidth / 2 + translationX - mTargetWidth * mScaleX / 2
//        val viewY = mTargetHeight / 2 + translationY - mTargetHeight * mScaleY / 2
//        view.x = viewX
//        view.y = viewY
//
//        val centerX = view.x + dragPhotoViewInfo.originWidth / 2
//        val centerY = view.y + dragPhotoViewInfo.originHeight / 2
//
//        val translateX = dragPhotoViewInfo.originCenterX - centerX
//        val translateY = dragPhotoViewInfo.originCenterY - centerY
//
//
//        val translateXAnimator = ValueAnimator.ofFloat(view.x, view.x + translateX)
//        translateXAnimator.addUpdateListener { valueAnimator -> view.x = valueAnimator.animatedValue as Float }
//        translateXAnimator.duration = 300
//        translateXAnimator.start()
//        val translateYAnimator = ValueAnimator.ofFloat(view.y, view.y + translateY)
//        translateYAnimator.addUpdateListener { valueAnimator -> view.y = valueAnimator.animatedValue as Float }
//        translateYAnimator.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animator: Animator) {
//
//            }
//
//            override fun onAnimationEnd(animator: Animator) {
//                animator.removeAllListeners()
//                finish()
//                overridePendingTransition(0, 0)
//            }
//
//            override fun onAnimationCancel(animator: Animator) {
//
//            }
//
//            override fun onAnimationRepeat(animator: Animator) {
//
//            }
//        })
//        translateYAnimator.duration = 300
//        translateYAnimator.start()
    }

    override fun start() {

    }

    override fun finish() {

    }

}
