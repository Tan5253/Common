package com.like.common.view.dragvideoview

import android.databinding.DataBindingUtil
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.R
import com.like.common.databinding.ActivityDragVideoViewBinding
import com.like.common.util.ImageLoaderUtils

class DragVideoViewActivity : BaseActivity() {
    companion object {
        const val KEY_DATA = "dragVideoViewInfo"
    }

    private val mBinding: ActivityDragVideoViewBinding by lazy {
        DataBindingUtil.setContentView<ActivityDragVideoViewBinding>(this, R.layout.activity_drag_video_view)
    }

    private val mImageLoaderUtils: ImageLoaderUtils by lazy { ImageLoaderUtils(this) }

    private lateinit var dragVideoViewInfo: DragVideoViewInfo

    override fun getViewModel(): BaseViewModel? {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getColor(int id)在API版本23时(Android 6.0)已然过时
            //从这里我们可以看出，当API版本>=23时，使用ContextCompatApi23.getColor(context, id)方法，
            //当API版本<23时，使用context.getResources().getColor(id)方法获取相应色值
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            window.statusBarColor = ContextCompat.getColor(this, R.color.common_transparent)
        }

        dragVideoViewInfo = intent.getParcelableExtra(KEY_DATA)

        if (dragVideoViewInfo.imageResId > 0) {
            mBinding.iv.setImageResource(dragVideoViewInfo.imageResId)
        } else {
            mImageLoaderUtils.display(dragVideoViewInfo.imageUrl, mBinding.iv)
        }

        mBinding.rlVideo.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mBinding.rlVideo.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                mPhotoViews[curClickedIndex].enter()
            }
        })
        return null
    }

//    override fun onBackPressed() {
//        mPhotoViews[mViewPager.currentItem].disappear()
//    }
}