package com.like.common.sample

import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import android.view.WindowManager
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityDragphotoviewBinding
import com.like.common.util.ResourceUtils
import com.like.common.util.StorageUtils
import com.like.common.view.dragphotoview.DragPhotoViewActivity
import com.like.common.view.dragphotoview.DragPhotoViewInfo
import com.like.common.view.dragvideoview.DragVideoViewActivity
import com.like.common.view.dragvideoview.DragVideoViewInfo
import java.util.*

class DragViewTestActivity : BaseActivity() {
    private val mBinding: ActivityDragphotoviewBinding by lazy {
        DataBindingUtil.setContentView<ActivityDragphotoviewBinding>(this, R.layout.activity_dragphotoview)
    }

    override fun getViewModel(): BaseViewModel? {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mBinding
        findViewById(R.id.iv_0)
        return null
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.iv_0, R.id.iv_1, R.id.iv_2 -> {
                val list = ArrayList<DragPhotoViewInfo>()
                /**
                 * view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                 * dragPhotoView.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                 * getLocationOnScreen计算该视图在全局坐标系中的x，y值，
                 * (注意这个值是要从屏幕顶端算起，也就是索包括了通知栏的高度)//获取在当前屏幕内的绝对坐标
                 * location [0]--->x坐标,location [1]--->y坐标
                 *
                 */
                val location0 = IntArray(2)
                mBinding.iv0.getLocationOnScreen(location0)
                list.add(DragPhotoViewInfo(location0[0], location0[1], mBinding.iv0.width, mBinding.iv0.height, "", R.drawable.video_image_1, view.id == R.id.iv_0))

                val location1 = IntArray(2)
                mBinding.iv1.getLocationOnScreen(location1)
                list.add(DragPhotoViewInfo(location1[0], location1[1], mBinding.iv1.width, mBinding.iv1.height, "", R.drawable.wugeng1, view.id == R.id.iv_1))

                val location2 = IntArray(2)
                mBinding.iv2.getLocationOnScreen(location2)
                list.add(DragPhotoViewInfo(location2[0], location2[1], mBinding.iv2.width, mBinding.iv2.height, "", R.drawable.wugeng2, view.id == R.id.iv_2))

                val intent = Intent(this, DragPhotoViewActivity::class.java)
                intent.putParcelableArrayListExtra(DragPhotoViewActivity.KEY_DATA, list)
                startActivity(intent)
            }
            R.id.rl_video -> {
                val location0 = IntArray(2)
                mBinding.rlVideo.getLocationOnScreen(location0)

                val sdPath = "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/video_1.mp4"
                ResourceUtils.Assets2Sd(this, "video_1.mp4", sdPath)
                val intent = Intent(this, DragVideoViewActivity::class.java)
                intent.putExtra(DragVideoViewActivity.KEY_DATA, DragVideoViewInfo(location0[0], location0[1], mBinding.rlVideo.width, mBinding.rlVideo.height, "", R.drawable.video_image_1, sdPath))
                startActivity(intent)
            }
        }
        overridePendingTransition(0, 0)
    }

}
