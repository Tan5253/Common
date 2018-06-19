package com.like.common.sample

import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import android.view.WindowManager
import com.like.common.base.context.BaseActivity
import com.like.common.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityDragphotoviewBinding
import com.like.common.util.GlideUtils
import com.like.common.util.ResourceUtils
import com.like.common.util.StorageUtils
import com.like.common.view.dragview.entity.DragInfo
import com.like.common.view.dragview.view.DragViewActivity
import java.io.File
import java.util.*

class DragViewTestActivity : BaseActivity() {
    private val originImageUrl0: String by lazy { "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/image_0_origin.jpg" }
    private val originImageUrl1: String by lazy { "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/image_1_origin.jpg" }
    private val originImageUrl2: String by lazy { "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/image_2_origin.jpg" }
    private val imageUrl0: String by lazy { "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/image_0.jpg" }
    private val imageUrl1: String by lazy { "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/image_1.jpg" }
    private val imageUrl2: String by lazy { "${StorageUtils.InternalStorageHelper.getBaseDir(this)}/image_2.jpg" }
    private val videoUrl: String by lazy { "${StorageUtils.InternalStorageHelper.getBaseDir(this)}${File.separator}Video/video_1.mp4" }
    private val videoImageUrl: String by lazy { imageUrl0 }

//    private val originImageUrl0: String by lazy { "http://bbsfiles.vivo.com.cn/vivobbs/attachment/forum/201601/11/183844md4d33a5d8d134w4.jpg.thumb.jpg" }
//    private val originImageUrl1: String by lazy { "http://bbsfiles.vivo.com.cn/vivobbs/attachment/forum/201601/11/183837avftmmzmfi76tkis.jpg.thumb.jpg" }
//    private val originImageUrl2: String by lazy { "http://bbsfiles.vivo.com.cn/vivobbs/attachment/forum/201601/11/183832tqu8fyy6qqk8a8f0.jpg.thumb.jpg" }
//    private val imageUrl0: String by lazy { "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507451649503&di=f17e048c5bca2efc2879dc8e578a73f1&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fexp%2Fw%3D500%2Fsign%3D03fdfcc6d743ad4ba62e46c0b2035a89%2F8ad4b31c8701a18b5d18132c982f07082938fee6.jpg" }
//    private val imageUrl1: String by lazy { "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507451649502&di=f6f24a7a3b395e1107b8ddceb3efe2bb&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F4034970a304e251f89ac4497a186c9177f3e5301.jpg" }
//    private val imageUrl2: String by lazy { "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507451649502&di=112f816b18998d14223ab58269f5c057&imgtype=0&src=http%3A%2F%2Fupload.gezila.com%2Fdata%2F20150906%2F28201441522774.jpg" }
//    private val videoUrl: String by lazy { "http://he.yinyuetai.com/uploads/videos/common/31BA015D2B8D04657E61B4BF0B448B79.mp4" }
//    private val videoImageUrl: String by lazy { "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507451649502&di=67866dc8e157120fb44487329a18c3c9&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fexp%2Fw%3D480%2Fsign%3D788267e49c2f07085f052b08d925b865%2F9922720e0cf3d7ca849f4addf01fbe096b63a934.jpg" }

    private val mBinding: ActivityDragphotoviewBinding by lazy {
        DataBindingUtil.setContentView<ActivityDragphotoviewBinding>(this, R.layout.activity_dragphotoview)
    }

    private val glideUtils: GlideUtils by lazy { GlideUtils(this) }

    override fun getViewModel(): BaseViewModel? {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mBinding
        findViewById(R.id.iv_0)

        ResourceUtils.Assets2Sd(this, "video_1.mp4", videoUrl)
        ResourceUtils.Assets2Sd(this, "image_0_origin.jpg", originImageUrl0)
        ResourceUtils.Assets2Sd(this, "image_1_origin.jpg", originImageUrl1)
        ResourceUtils.Assets2Sd(this, "image_2_origin.jpg", originImageUrl2)
        ResourceUtils.Assets2Sd(this, "image_0.jpg", imageUrl0)
        ResourceUtils.Assets2Sd(this, "image_1.jpg", imageUrl1)
        ResourceUtils.Assets2Sd(this, "image_2.jpg", imageUrl2)

        glideUtils.display(imageUrl0, mBinding.iv0)
        glideUtils.display(imageUrl1, mBinding.iv1)
        glideUtils.display(imageUrl2, mBinding.iv2)
        glideUtils.display(videoImageUrl, mBinding.iv3)
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
                list.add(DragInfo(location0[0].toFloat(), location0[1].toFloat(), mBinding.iv0.width.toFloat(), mBinding.iv0.height.toFloat(), thumbImageUrl = imageUrl0, imageUrl = originImageUrl0, isClicked = view.id == R.id.iv_0))

                val location1 = IntArray(2)
                mBinding.iv1.getLocationOnScreen(location1)
                list.add(DragInfo(location1[0].toFloat(), location1[1].toFloat(), mBinding.iv1.width.toFloat(), mBinding.iv1.height.toFloat(), thumbImageUrl = imageUrl1, imageUrl = originImageUrl1, isClicked = view.id == R.id.iv_1))

                val location2 = IntArray(2)
                mBinding.iv2.getLocationOnScreen(location2)
                list.add(DragInfo(location2[0].toFloat(), location2[1].toFloat(), mBinding.iv2.width.toFloat(), mBinding.iv2.height.toFloat(), thumbImageUrl = imageUrl2, imageUrl = originImageUrl2, isClicked = view.id == R.id.iv_2))

                intent.putParcelableArrayListExtra(DragViewActivity.KEY_DATA, list)
            }
            R.id.rl_video -> {
                val location0 = IntArray(2)
                mBinding.rlVideo.getLocationOnScreen(location0)
                intent.putExtra(DragViewActivity.KEY_DATA,
                        DragInfo(location0[0].toFloat(),
                                location0[1].toFloat(),
                                mBinding.rlVideo.width.toFloat(),
                                mBinding.rlVideo.height.toFloat(),
                                thumbImageUrl = videoImageUrl,
                                videoUrl = videoUrl))
            }
        }
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

}
