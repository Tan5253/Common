package com.like.common.view.dragphotoview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * 从缩放状态退出DragPhotoViewActivity的动画
 */
class ExitAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    private var pendingTranslateX = 0f
    private var pendingTranslateY = 0f

    fun setData(curTranslationX: Float, curTranslationY: Float): ExitAnimationManager {
        // 把缩放后的dragPhotoView移动到初始位置，并刷新。这一步是为了解决拖拽时有可能导致dragPhotoView显示的图片不完整（被屏幕边缘剪切了）。
        // 如果不做处理，只需要下面的代码
//        val newViewX = mTargetWidth / 2 + x - mTargetWidth * mScaleX / 2
//        val newViewY = mTargetHeight / 2 + y - mTargetHeight * mScaleY / 2
//        val translateXAnimator = ValueAnimator.ofFloat(0f, dragPhotoViewInfo.originLeft.toFloat() - newViewX)
//        val translateYAnimator = ValueAnimator.ofFloat(0f, dragPhotoViewInfo.originTop.toFloat() - newViewY)
        dragPhotoView.mRestoreAnimationManager.canvasTranslationX = -dragPhotoView.width / 2 + dragPhotoView.width * dragPhotoView.mRestoreAnimationManager.canvasScale / 2
        dragPhotoView.mRestoreAnimationManager.canvasTranslationY = -dragPhotoView.height / 2 + dragPhotoView.height * dragPhotoView.mRestoreAnimationManager.canvasScale / 2
        dragPhotoView.invalidate()
        // 把缩放后的dragPhotoView移动到手指释放时的位置，准备开始动画。
        dragPhotoView.x = dragPhotoView.mWidth / 2 + curTranslationX - dragPhotoView.mWidth * dragPhotoView.mRestoreAnimationManager.canvasScale / 2
        dragPhotoView.y = dragPhotoView.mHeight / 2 + curTranslationY - dragPhotoView.mHeight * dragPhotoView.mRestoreAnimationManager.canvasScale / 2
        // 计算缩放后的dragPhotoView和原始的dragPhotoView的位移
        val curCenterX = dragPhotoView.x + dragPhotoViewInfo.originWidth / 2
        val curCenterY = dragPhotoView.y + dragPhotoViewInfo.originHeight / 2
        pendingTranslateX = dragPhotoViewInfo.originCenterX - curCenterX
        pendingTranslateY = dragPhotoViewInfo.originCenterY - curCenterY
        return this
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ObjectAnimator.ofFloat(dragPhotoView, "x", dragPhotoView.x, dragPhotoView.x + pendingTranslateX))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "y", dragPhotoView.y, dragPhotoView.y + pendingTranslateY))
    }

    override fun onEnd() {
        val activity = dragPhotoView.context
        if (activity is Activity) {
            activity.finish()
            activity.overridePendingTransition(0, 0)
        }
    }

}
