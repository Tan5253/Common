package com.like.common.view.dragphotoview

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.R

class DragPhotoViewActivity : BaseActivity() {
    companion object {
        const val KEY_DATA = "DragPhotoViewInfo"
    }

    private val mViewPager: FixMultiViewPager by lazy { FixMultiViewPager(this) }
    private val mImageUrlList = listOf("url", "url", "url")
    private val mPhotoViews = ArrayList<DragPhotoView>()

    private lateinit var dragPhotoViewInfo: DragPhotoViewInfo
    private var mTargetHeight: Float = 0f
    private var mTargetWidth: Float = 0f
    private var mScaleX: Float = 0f
    private var mScaleY: Float = 0f
    private var mTranslationX: Float = 0f
    private var mTranslationY: Float = 0f

    override fun getViewModel(): BaseViewModel? {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getColor(int id)在API版本23时(Android 6.0)已然过时
            //从这里我们可以看出，当API版本>=23时，使用ContextCompatApi23.getColor(context, id)方法，
            //当API版本<23时，使用context.getResources().getColor(id)方法获取相应色值
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            window.statusBarColor = ContextCompat.getColor(this, R.color.common_transparent)
        }

        setContentView(mViewPager)

        dragPhotoViewInfo = intent.getSerializableExtra(KEY_DATA) as DragPhotoViewInfo

        mImageUrlList.mapTo(mPhotoViews) {
            DragPhotoView(this, dragPhotoViewInfo).apply {
                setImageResource(R.drawable.wugeng)
                mTapListener = object : OnTapListener {
                    override fun onTap(view: DragPhotoView) {
                        finishWithAnimation()
                    }
                }
                mExitListener = object : OnExitListener {
                    override fun onExit(view: DragPhotoView, x: Float, y: Float, w: Float, h: Float) {
                        performExitAnimation(view, x, y, w, h)
                    }
                }
            }
        }

        mViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View?, `object`: Any?) = view === `object`
            override fun getCount() = mImageUrlList.size
            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                container?.addView(mPhotoViews[position])
                return mPhotoViews[position]
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container?.removeView(mPhotoViews[position])
            }
        }

        mViewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val location = IntArray(2)

                val photoView = mPhotoViews[0]
                photoView.getLocationOnScreen(location)

                mTargetHeight = photoView.height.toFloat()
                mTargetWidth = photoView.width.toFloat()
                mScaleX = dragPhotoViewInfo.originWidth.toFloat() / mTargetWidth
                mScaleY = dragPhotoViewInfo.originHeight.toFloat() / mTargetHeight

                val targetCenterX = location[0] + mTargetWidth / 2
                val targetCenterY = location[1] + mTargetHeight / 2

                mTranslationX = dragPhotoViewInfo.originCenterX - targetCenterX
                mTranslationY = dragPhotoViewInfo.originCenterY - targetCenterY
                photoView.translationX = mTranslationX
                photoView.translationY = mTranslationY

                photoView.scaleX = mScaleX
                photoView.scaleY = mScaleY

//                performEnterAnimation()

                photoView.mEnterAnimationManager.start()

                for (i in mPhotoViews.indices) {
                    mPhotoViews[i].mRestoreAnimationManager.minScale = mScaleX
                }
            }
        })

        return null
    }

    private fun performExitAnimation(view: DragPhotoView, x: Float, y: Float, w: Float, h: Float) {
        view.mRestoreAnimationManager.finish()
        val viewX = mTargetWidth / 2 + x - mTargetWidth * mScaleX / 2
        val viewY = mTargetHeight / 2 + y - mTargetHeight * mScaleY / 2
        view.x = viewX
        view.y = viewY

        val centerX = view.x + dragPhotoViewInfo.originWidth / 2
        val centerY = view.y + dragPhotoViewInfo.originHeight / 2

        val translateX = dragPhotoViewInfo.originCenterX - centerX
        val translateY = dragPhotoViewInfo.originCenterY - centerY


        val translateXAnimator = ValueAnimator.ofFloat(view.x, view.x + translateX)
        translateXAnimator.addUpdateListener { valueAnimator -> view.x = valueAnimator.animatedValue as Float }
        translateXAnimator.duration = 300
        translateXAnimator.start()
        val translateYAnimator = ValueAnimator.ofFloat(view.y, view.y + translateY)
        translateYAnimator.addUpdateListener { valueAnimator -> view.y = valueAnimator.animatedValue as Float }
        translateYAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                animator.removeAllListeners()
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        translateYAnimator.duration = 300
        translateYAnimator.start()
    }

    private fun finishWithAnimation() {
        val photoView = mPhotoViews[0]
        val translateXAnimator = ValueAnimator.ofFloat(0f, mTranslationX)
        translateXAnimator.addUpdateListener { valueAnimator -> photoView.x = valueAnimator.animatedValue as Float }
        translateXAnimator.duration = 300
        translateXAnimator.start()

        val translateYAnimator = ValueAnimator.ofFloat(0f, mTranslationY)
        translateYAnimator.addUpdateListener { valueAnimator -> photoView.y = valueAnimator.animatedValue as Float }
        translateYAnimator.duration = 300
        translateYAnimator.start()

        val scaleYAnimator = ValueAnimator.ofFloat(1f, mScaleY)
        scaleYAnimator.addUpdateListener { valueAnimator -> photoView.scaleY = valueAnimator.animatedValue as Float }
        scaleYAnimator.duration = 300
        scaleYAnimator.start()

        val scaleXAnimator = ValueAnimator.ofFloat(1f, mScaleX)
        scaleXAnimator.addUpdateListener { valueAnimator -> photoView.scaleX = valueAnimator.animatedValue as Float }

        scaleXAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                animator.removeAllListeners()
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        scaleXAnimator.duration = 300
        scaleXAnimator.start()
    }

    private fun performEnterAnimation() {
        val photoView = mPhotoViews[0]
        val translateXAnimator = ValueAnimator.ofFloat(photoView.x, 0f)
        translateXAnimator.addUpdateListener { valueAnimator -> photoView.x = valueAnimator.animatedValue as Float }
        translateXAnimator.duration = 300
        translateXAnimator.start()

        val translateYAnimator = ValueAnimator.ofFloat(photoView.y, 0f)
        translateYAnimator.addUpdateListener { valueAnimator -> photoView.y = valueAnimator.animatedValue as Float }
        translateYAnimator.duration = 300
        translateYAnimator.start()

        val scaleYAnimator = ValueAnimator.ofFloat(mScaleY, 1f)
        scaleYAnimator.addUpdateListener { valueAnimator -> photoView.scaleY = valueAnimator.animatedValue as Float }
        scaleYAnimator.duration = 300
        scaleYAnimator.start()

        val scaleXAnimator = ValueAnimator.ofFloat(mScaleX, 1f)
        scaleXAnimator.addUpdateListener { valueAnimator -> photoView.scaleX = valueAnimator.animatedValue as Float }
        scaleXAnimator.duration = 300
        scaleXAnimator.start()
    }

    override fun onBackPressed() {
        finishWithAnimation()
    }
}
