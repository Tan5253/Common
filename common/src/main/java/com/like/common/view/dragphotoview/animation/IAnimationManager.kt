package com.like.common.view.dragphotoview.animation

interface IAnimationManager {
    companion object {
        const val DURATION = 300L
        const val MAX_RESTORE_ANIMATOR_TRANSLATE_Y = 500
    }

    fun start()
    fun finish()
}
