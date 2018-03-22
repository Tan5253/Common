package com.like.common.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.like.base.context.BaseActivity;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.databinding.ActivityTakePhotoBinding;
import com.like.common.util.GlideUtils;
import com.like.common.util.RxBusTag;
import com.like.common.util.TakePhotoUtils;
import com.like.rxbus.annotations.RxBusSubscribe;
import com.like.toast.ToastUtilsKt;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 拍照测试
 *
 * @author like
 * @version 1.0
 *          created on 2017/5/31 10:38
 */
public class TakePhotoActivity extends BaseActivity {
    private ActivityTakePhotoBinding mBinding;
    private TakePhotoUtils mTakePhotoUtils;

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_photo);
        mTakePhotoUtils = new TakePhotoUtils(this);
        return null;
    }

    @SuppressLint("MissingPermission")
    public void takePhoto(View view) {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtilsKt.shortToastCenter(getApplicationContext(), "弹窗");
            } else {
                EasyPermissions.requestPermissions(this, "需要读写存储卡和照相权限", 1111, Manifest.permission.CAMERA);
            }
        } else {
            mTakePhotoUtils.takePhoto();
        }
    }

    @SuppressLint("MissingPermission")
    public void pickPhoto(View view) {
        mTakePhotoUtils.pickPhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhotoUtils.handleResult(requestCode, resultCode, data);
    }

    // 照相成功、从相册选择图片成功、裁剪成功
    @RxBusSubscribe(RxBusTag.TAG_CROP_PHOTO_SUCCESS)
    public void onTakePhotoSuccess(String filePath) {
        new GlideUtils(this).displayCircle(filePath, mBinding.ivTakePhoto);
    }

//    /**
//     * 重写onRequestPermissionsResult，用于接受请求结果
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //将请求结果传递EasyPermission库处理
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }

//    /**
//     * 请求权限成功
//     *
//     * @param requestCode
//     * @param perms
//     */
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        ToastUtilsKt.shortToastCenter(getApplicationContext(), "用户授权成功");
//    }
//
//    /**
//     * 请求权限失败
//     *
//     * @param requestCode
//     * @param perms
//     */
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        /**
//         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
//         * 这时候，需要跳转到设置界面去，让用户手动开启。
//         */
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//        }
//    }

}
