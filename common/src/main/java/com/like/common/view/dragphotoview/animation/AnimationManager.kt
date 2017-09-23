package com.like.common.view.dragphotoview.animation

import android.animation.AnimatorSet
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

abstract class AnimationManager(val dragPhotoView: DragPhotoView, val dragPhotoViewInfo: DragPhotoViewInfo) {
    companion object {
        const val DURATION = 300L
        const val MAX_RESTORE_ANIMATOR_TRANSLATE_Y = 500
    }

    val animatorSet: AnimatorSet = AnimatorSet()

    abstract fun start()

    abstract fun finish()
}
