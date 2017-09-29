package com.like.common.view.dragview.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.BaseDragView

/**
 * 从缩放状态到退出Activity的动画
 */
class ExitAnimationManager(view: BaseDragView, info: DragInfo) : BaseAnimationManager(view, info) {
    private val halfDragPhotoViewWidth = view.width / 2
    private val halfDragPhotoViewHeight = view.height / 2
    private var halfScaleDragPhotoViewWidth = view.width * view.mRestoreAnimationManager.canvasScale / 2
    private var halfScaleDragPhotoViewHeight = view.height * view.mRestoreAnimationManager.canvasScale / 2
    // 注意：这里是以缩放后的视图为初始情况
    private var pendingScaleX = info.originWidth / (view.width.toFloat() * view.mRestoreAnimationManager.canvasScale)
    private var pendingScaleY = info.originHeight / (view.height.toFloat() * view.mRestoreAnimationManager.canvasScale)
    private var pendingLeft = info.originLeft
    private var pendingTop = info.originTop

    fun setCurData(info: DragInfo) {
        this.info = info
        // 根据DragInfo重新计算数据，因为有ViewPager的影响
        halfScaleDragPhotoViewWidth = view.width * view.mRestoreAnimationManager.canvasScale / 2
        halfScaleDragPhotoViewHeight = view.height * view.mRestoreAnimationManager.canvasScale / 2
        pendingScaleX = info.originWidth / (view.width.toFloat() * view.mRestoreAnimationManager.canvasScale)
        pendingScaleY = info.originHeight / (view.height.toFloat() * view.mRestoreAnimationManager.canvasScale)
        pendingLeft = info.originLeft
        pendingTop = info.originTop
    }

    fun setTranslationData(curTranslationX: Float, curTranslationY: Float): ExitAnimationManager {
        // 把缩放后的view移动到屏幕左上角的位置，这样能保证不管是否缩放，都能显示完整的图片，并刷新。这一步是为了解决拖拽时有可能导致view显示的图片不完整（被屏幕边缘剪切了）。
        // 如果不做处理，只需要下面的代码
//        val newViewX = halfDragPhotoViewWidth + curTranslationX - halfScaleDragPhotoViewWidth
//        val newViewY = halfDragPhotoViewHeight + curTranslationY - halfScaleDragPhotoViewHeight
//        val translateXAnimator = ValueAnimator.ofFloat(0f, info.originLeft.toFloat() - newViewX)
//        val translateYAnimator = ValueAnimator.ofFloat(0f, info.originTop.toFloat() - newViewY)
        view.mRestoreAnimationManager.canvasTranslationX = -halfDragPhotoViewWidth + halfScaleDragPhotoViewWidth
        view.mRestoreAnimationManager.canvasTranslationY = -halfDragPhotoViewHeight + halfScaleDragPhotoViewHeight
        view.mRestoreAnimationManager.canvasBgAlpha = 0
        view.invalidate()
        // 把缩放后的view移动到手指释放时的位置，准备开始动画。
        view.x = halfDragPhotoViewWidth + curTranslationX - halfScaleDragPhotoViewWidth
        view.y = halfDragPhotoViewHeight + curTranslationY - halfScaleDragPhotoViewHeight
        return this
    }

    override fun fillAnimatorSet(animatorSet: AnimatorSet) {
        view.pivotX = 0f
        view.pivotY = 0f
        animatorSet.play(ObjectAnimator.ofFloat(view, "x", view.x, pendingLeft))
                .with(ObjectAnimator.ofFloat(view, "y", view.y, pendingTop))
                .with(ObjectAnimator.ofFloat(view, "scaleX", 1f, pendingScaleX))// 注意：这里是以缩放后的视图为初始情况，所以开始为1f
                .with(ObjectAnimator.ofFloat(view, "scaleY", 1f, pendingScaleY))
    }

    override fun onEnd() {
        finishActivity()
    }

}