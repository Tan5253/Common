package com.like.common.view.dragphotoview.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo
import com.like.common.view.dragphotoview.OnExitListener

/**
 * 从正常状态退出DragPhotoViewActivity的动画
 */
class DisappearAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo, var mExitListener: OnExitListener? = null) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    var pendingTranslateX = 0f
    var pendingTranslateY = 0f
    var pendingScaleX = 0f
    var pendingScaleY = 0f

    init {
        pendingTranslateX = dragPhotoViewInfo.originCenterX - dragPhotoView.mWidth / 2
        pendingTranslateY = dragPhotoViewInfo.originCenterY - dragPhotoView.mHeight / 2
        pendingScaleX = dragPhotoViewInfo.originWidth / dragPhotoView.mWidth
        pendingScaleY = dragPhotoViewInfo.originHeight / dragPhotoView.mHeight
    }

    override fun start() {
        animatorSet.play(ValueAnimator.ofFloat(0f, pendingTranslateX).apply {
            duration = AnimationManager.DURATION
            addUpdateListener {
                dragPhotoView.x = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(0f, pendingTranslateY).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.y = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(1f, pendingScaleX).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.scaleX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(1f, pendingScaleY).apply {
                    duration = AnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.scaleY = it.animatedValue as Float
                    }
                })
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                animation?.removeAllListeners()
                mExitListener?.onExitFinish()
            }
        })
        animatorSet.start()
    }

    override fun finish() {
    }

}