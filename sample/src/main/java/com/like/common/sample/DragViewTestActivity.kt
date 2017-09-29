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
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.DragViewActivity
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
        val intent = Intent(this, DragViewActivity::class.java)
        when (view.id) {
            R.id.iv_0, R.id.iv_1, R.id.iv_2 -> {
                val list = ArrayList<DragInfo>()
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
                list.add(DragInfo(location0[0].toFloat(), location0[1].toFloat(), mBinding.iv0.width.toFloat(), mBinding.iv0.height.toFloat(), thumbImageResId = R.drawable.video_image_1, imageResId = R.drawable.video_image_1, isClicked = view.id == R.id.iv_0))

                val location1 = IntArray(2)
                mBinding.iv1.getLocationOnScreen(location1)
                list.add(DragInfo(location1[0].toFloat(), location1[1].toFloat(), mBinding.iv1.width.toFloat(), mBinding.iv1.height.toFloat(), thumbImageResId = R.drawable.wugeng1, imageResId = R.drawable.wugeng1, isClicked = view.id == R.id.iv_1))

                val location2 = IntArray(2)
                mBinding.iv2.getLocationOnScreen(location2)
                list.add(DragInfo(location2[0].toFloat(), location2[1].toFloat(), mBinding.iv2.width.toFloat(), mBinding.iv2.height.toFloat(), thumbImageResId = R.drawable.wugeng2, imageResId = R.drawable.wugeng2, isClicked = view.id == R.id.iv_2))

                intent.putParcelableArrayListExtra(DragViewActivity.KEY_DATA, list)
            }
            R.id.rl_video -> {
                val location0 = IntArray(2)
                mBinding.rlVideo.getLocationOnScreen(location0)

                val sdPath = "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/video_1.mp4"
                ResourceUtils.Assets2Sd(this, "video_1.mp4", sdPath)
                intent.putExtra(DragViewActivity.KEY_DATA,
                        DragInfo(location0[0].toFloat(),
                                location0[1].toFloat(),
                                mBinding.rlVideo.width.toFloat(),
                                mBinding.rlVideo.height.toFloat(),
                                thumbImageResId = R.drawable.video_image_1,
                                videoUrl = sdPath))
            }
        }
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

}
