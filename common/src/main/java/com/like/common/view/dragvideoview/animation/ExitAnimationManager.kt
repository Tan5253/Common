package com.like.common.view.dragvideoview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import com.like.common.view.dragvideoview.DragVideoView
import com.like.common.view.dragvideoview.DragVideoViewInfo

/**
 * 从缩放状态到退出DragPhotoViewActivity的动画
 */
class ExitAnimationManager(dragVideoView: DragVideoView, dragVideoViewInfo: DragVideoViewInfo) : AnimationManager(dragVideoView, dragVideoViewInfo) {
    private val halfDragPhotoViewWidth = dragVideoView.width / 2
    private val halfDragPhotoViewHeight = dragVideoView.height / 2
    private val halfScaleDragPhotoViewWidth = dragVideoView.width * dragVideoView.mRestoreAnimationManager.canvasScale / 2
    private val halfScaleDragPhotoViewHeight = dragVideoView.height * dragVideoView.mRestoreAnimationManager.canvasScale / 2
    // 注意：这里是以缩放后的视图为初始情况
    private val pendingScaleX = dragVideoViewInfo.originWidth / (dragVideoView.width.toFloat() * dragVideoView.mRestoreAnimationManager.canvasScale)
    private val pendingScaleY = dragVideoViewInfo.originHeight / (dragVideoView.height.toFloat() * dragVideoView.mRestoreAnimationManager.canvasScale)

    fun setData(curTranslationX: Float, curTranslationY: Float): ExitAnimationManager {
        // 把缩放后的dragVideoView移动到屏幕左上角的位置，这样能保证不管是否缩放，都能显示完整的图片，并刷新。这一步是为了解决拖拽时有可能导致dragVideoView显示的图片不完整（被屏幕边缘剪切了）。
        // 如果不做处理，只需要下面的代码
//        val newViewX = halfDragPhotoViewWidth + curTranslationX - halfScaleDragPhotoViewWidth
//        val newViewY = halfDragPhotoViewHeight + curTranslationY - halfScaleDragPhotoViewHeight
//        val translateXAnimator = ValueAnimator.ofFloat(0f, dragVideoViewInfo.originLeft.toFloat() - newViewX)
//        val translateYAnimator = ValueAnimator.ofFloat(0f, dragVideoViewInfo.originTop.toFloat() - newViewY)
        dragVideoView.mRestoreAnimationManager.canvasTranslationX = -halfDragPhotoViewWidth + halfScaleDragPhotoViewWidth
        dragVideoView.mRestoreAnimationManager.canvasTranslationY = -halfDragPhotoViewHeight + halfScaleDragPhotoViewHeight
        dragVideoView.mRestoreAnimationManager.canvasBgAlpha = 0
        dragVideoView.invalidate()
        // 把缩放后的dragVideoView移动到手指释放时的位置，准备开始动画。
        dragVideoView.x = halfDragPhotoViewWidth + curTranslationX - halfScaleDragPhotoViewWidth
        dragVideoView.y = halfDragPhotoViewHeight + curTranslationY - halfScaleDragPhotoViewHeight
        return this
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        dragVideoView.pivotX = 0f
        dragVideoView.pivotY = 0f
        animatorSet.play(ObjectAnimator.ofFloat(dragVideoView, "x", dragVideoView.x, dragVideoViewInfo.originLeft.toFloat()))
                .with(ObjectAnimator.ofFloat(dragVideoView, "y", dragVideoView.y, dragVideoViewInfo.originTop.toFloat()))
                .with(ObjectAnimator.ofFloat(dragVideoView, "scaleX", 1f, pendingScaleX))// 注意：这里是以缩放后的视图为初始情况，所以开始为1f
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