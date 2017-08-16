package com.like.common.sample;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.View;

import com.like.base.context.BasePermissionActivity;
import com.like.base.viewmodel.BaseViewModel;
import com.like.common.sample.databinding.ActivityTakePhotoBinding;
import com.like.common.util.RxBusTag;
import com.like.common.util.TakePhotoUtils;
import com.like.logger.Logger;
import com.like.rxbus.annotations.RxBusSubscribe;

import java.io.File;

/**
 * 拍照测试
 *
 * @author like
 * @version 1.0
 *          created on 2017/5/31 10:38
 */
public class TakePhotoActivity extends BasePermissionActivity {
    private ActivityTakePhotoBinding mBinding;
    private TakePhotoUtils mTakePhotoUtils;

    @Override
    protected BaseViewModel getViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_photo);
        mTakePhotoUtils = TakePhotoUtils.getInstance(this).setCrop(true).setSquareCropBox(false).setCropHeight(100).setCropWidth(500);
        return null;
    }

    public void takePhoto(View view) {
        mTakePhotoUtils.takePhoto();
    }

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
    public void onTakePhotoSuccess(Pair<File, Bitmap> pair) {
        Logger.i(pair.first.getAbsolutePath());
        mBinding.ivTakePhoto.setImageBitmap(pair.second);
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
