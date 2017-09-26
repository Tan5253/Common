package com.like.common.view.dragphotoview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragphotoview.DragPhotoView
import com.like.common.view.dragphotoview.DragPhotoViewInfo

/**
 * 从缩放状态退出DragPhotoViewActivity的动画
 */
class ExitAnimationManager(dragPhotoView: DragPhotoView, dragPhotoViewInfo: DragPhotoViewInfo) : AnimationManager(dragPhotoView, dragPhotoViewInfo) {
    private val halfDragPhotoViewWidth = dragPhotoView.width / 2
    private val halfDragPhotoViewHeight = dragPhotoView.height / 2
    private val halfScaleDragPhotoViewWidth = dragPhotoView.width * dragPhotoView.mRestoreAnimationManager.canvasScale / 2
    private val halfScaleDragPhotoViewHeight = dragPhotoView.height * dragPhotoView.mRestoreAnimationManager.canvasScale / 2
    private var initTranslationX = 0f
    private var pendingTranslationX = 0f
    private var pendingTranslationY = 0f
    // 注意：这里是以缩放后的视图为初始情况
    private val pendingScaleX = dragPhotoViewInfo.originWidth / (dragPhotoView.width.toFloat() * dragPhotoView.mRestoreAnimationManager.canvasScale)
    private val pendingScaleY = dragPhotoViewInfo.originHeight / (dragPhotoView.height.toFloat() * dragPhotoView.mRestoreAnimationManager.canvasScale)

    fun setData(curTranslationX: Float, curTranslationY: Float): ExitAnimationManager {
        // 把缩放后的dragPhotoView移动到屏幕左上角的位置，这样能保证不管是否缩放，都能显示完整的图片，并刷新。这一步是为了解决拖拽时有可能导致dragPhotoView显示的图片不完整（被屏幕边缘剪切了）。
        // 如果不做处理，只需要下面的代码
//        val newViewX = halfDragPhotoViewWidth + curTranslationX - halfScaleDragPhotoViewWidth
//        val newViewY = halfDragPhotoViewHeight + curTranslationY - halfScaleDragPhotoViewHeight
//        val translateXAnimator = ValueAnimator.ofFloat(0f, dragPhotoViewInfo.originLeft.toFloat() - newViewX)
//        val translateYAnimator = ValueAnimator.ofFloat(0f, dragPhotoViewInfo.originTop.toFloat() - newViewY)
        dragPhotoView.mRestoreAnimationManager.canvasTranslationX = -halfDragPhotoViewWidth + halfScaleDragPhotoViewWidth
        dragPhotoView.mRestoreAnimationManager.canvasTranslationY = -halfDragPhotoViewHeight + halfScaleDragPhotoViewHeight
        dragPhotoView.mRestoreAnimationManager.canvasBgAlpha = 0
        dragPhotoView.invalidate()
        // 把缩放后的dragPhotoView移动到手指释放时的位置，准备开始动画。
        dragPhotoView.x = halfDragPhotoViewWidth + curTranslationX - halfScaleDragPhotoViewWidth
        dragPhotoView.y = halfDragPhotoViewHeight + curTranslationY - halfScaleDragPhotoViewHeight
        // 计算缩放后的dragPhotoView距离原始的dragPhotoView的位移
        val curCenterX = dragPhotoView.x + halfScaleDragPhotoViewWidth
        val curCenterY = dragPhotoView.y + halfScaleDragPhotoViewHeight
        initTranslationX = dragPhotoView.width.toFloat() * dragPhotoViewInfo.index + dragPhotoView.x// 要考虑ViewPager切换页面对初始值的影响
        pendingTranslationX = dragPhotoViewInfo.originCenterX - curCenterX
        pendingTranslationY = dragPhotoViewInfo.originCenterY - curCenterY
        return this
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.play(ObjectAnimator.ofFloat(dragPhotoView, "x", initTranslationX, initTranslationX + pendingTranslationX))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "y", dragPhotoView.y, dragPhotoView.y + pendingTranslationY))
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleX", 1f, pendingScaleX))// 注意：这里是以缩放后的视图为初始情况，所以开始为1f
                .with(ObjectAnimator.ofFloat(dragPhotoView, "scaleY", 1f, pendingScaleY))
    }

    override fun onEnd() {
//        val activity = dragPhotoView.context
//        if (activity is Activity) {
//            activity.finish()
//            activity.overridePendingTransition(0, 0)
//        }
    }

}
