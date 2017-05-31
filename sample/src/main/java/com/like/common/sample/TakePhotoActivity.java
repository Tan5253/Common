package com.like.common.sample;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.like.base.context.BasePermissionActivity;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.databinding.ActivityTakePhotoBinding;
import com.like.common.util.RxBusTag;
import com.like.common.util.TakePhotoUtils;
import com.like.rxbus.annotations.RxBusSubscribe;

/**
 * 拍照测试
 *
 * @author like
 * @version 1.0
 *          created on 2017/5/31 10:38
 */
public class TakePhotoActivity extends BasePermissionActivity {
    private ActivityTakePhotoBinding mBinding;

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_photo);
        mBinding.ivTakePhoto.setOnClickListener(v -> TakePhotoUtils.getInstance(TakePhotoActivity.this).takePhoto(1));
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePhotoUtils.getInstance(this).handleResult(requestCode, resultCode, data);
    }

    @RxBusSubscribe(RxBusTag.TAG_TAKE_PHOTO_SUCCESS)
    public void onTakePhotoSuccess(TakePhotoUtils.TakePhotoResult takePhotoResult) {
        mBinding.ivTakePhoto.setImageBitmap(takePhotoResult.bitmap);
    }

    @Override
    protected String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA
        };
    }

    @Override
    protected String getRationale() {
        return "您需要允许以下权限才能使用应用："
                + "\n" + "1、存储权限：为了缓存应用的临时数据。"
                + "\n" + "2、相机权限：为了拍照上传头像。";
    }

    @Override
    protected void hasPermissions() {
    }
}
