package com.like.common.sample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityTakePhotoBinding
import com.like.common.util.GlideUtils
import com.like.common.util.PermissionUtils
import com.like.common.util.RxBusTag
import com.like.common.util.TakePhotoUtils
import com.like.rxbus.annotations.RxBusSubscribe

/**
 * 拍照测试
 *
 * @author like
 * @version 1.0
 * created on 2017/5/31 10:38
 */
class TakePhotoActivity : BaseActivity() {
    private var mBinding: ActivityTakePhotoBinding? = null
    private var mTakePhotoUtils: TakePhotoUtils? = null

    override fun getViewModel(): BaseViewModel? {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_photo)
        mTakePhotoUtils = TakePhotoUtils(this)
        return null
    }

    @SuppressLint("MissingPermission")
    fun takePhoto(view: View) {
        PermissionUtils.checkPermissionsAndRun(this,
                "需要读写存储卡和照相权限",
                11111,
                { mTakePhotoUtils!!.takePhoto() },
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    @SuppressLint("MissingPermission")
    fun pickPhoto(view: View) {
        mTakePhotoUtils!!.pickPhoto()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mTakePhotoUtils!!.handleResult(requestCode, resultCode, data)
    }

    // 照相成功、从相册选择图片成功、裁剪成功
    @RxBusSubscribe(RxBusTag.TAG_CROP_PHOTO_SUCCESS)
    fun onTakePhotoSuccess(filePath: String) {
        GlideUtils(this).displayCircle(filePath, mBinding!!.ivTakePhoto)
    }

//    /**
//     * 重写onRequestPermissionsResult，用于接受请求结果
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        //将请求结果传递EasyPermission库处理
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }
//
//    /**
//     * 请求权限成功
//     *
//     * @param requestCode
//     * @param perms
//     */
//    fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
//        applicationContext.shortToastCenter("用户授权成功")
//    }
//
//    /**
//     * 请求权限失败
//     *
//     * @param requestCode
//     * @param perms
//     */
//    fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
//        /**
//         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
//         * 这时候，需要跳转到设置界面去，让用户手动开启。
//         */
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            AppSettingsDialog.Builder(this).build().show()
//        }
//    }

}
