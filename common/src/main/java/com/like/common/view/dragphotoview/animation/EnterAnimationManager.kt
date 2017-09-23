package com.like.common.view.dragphotoview.animation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * 进入DragPhotoViewActivity的动画
 */
class EnterAnimationManager(val dragPhotoView: DragPhotoView) : IAnimationManager {
    lateinit var dragPhotoViewInfo: DragPhotoViewInfo
    private val enterAnimatorSet: AnimatorSet = AnimatorSet()

    override fun start() {
        enterAnimatorSet.play(ValueAnimator.ofFloat(dragPhotoView.x, 0f).apply {
            duration = IAnimationManager.DURATION
            addUpdateListener {
                dragPhotoView.x = it.animatedValue as Float
            }
        })
                .with(ValueAnimator.ofFloat(dragPhotoView.y, 0f).apply {
                    duration = IAnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.y = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(dragPhotoView.scaleX, 1f).apply {
                    duration = IAnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.scaleX = it.animatedValue as Float
                    }
                })
                .with(ValueAnimator.ofFloat(dragPhotoView.scaleY, 1f).apply {
                    duration = IAnimationManager.DURATION
                    addUpdateListener {
                        dragPhotoView.scaleY = it.animatedValue as Float
                    }
                })
        enterAnimatorSet.start()
    }

    override fun finish() {

    }

}
