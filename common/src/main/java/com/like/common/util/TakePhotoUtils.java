package com.like.common.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.like.rxbus.RxBus;
import com.like.toast.ToastUtilsKt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 照相相关工具类，可以照相、从相册获取图片、裁剪。支持android 7.0
 * <p>
 * {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
 *
 * @author like
 * @version 1.0
 *          created on 2017/4/19 9:24
 */
public class TakePhotoUtils {
    public static final int DEFAULT_CROP_WIDTH = 400;
    public static final int DEFAULT_CROP_HEIGHT = 600;
    // 打开相机的返回码
    public static final int REQUEST_CODE_TAKE_PHOTO = 100;
    // 打开相册的返回码
    public static final int REQUEST_CODE_SELECT_PICTURE = 101;
    // 剪切图片的返回码
    public static final int REQUEST_CODE_CROP_PICTURE = 102;

    public static String FILE_CONTENT_FILEPROVIDER;// 7.0  ContentUri
    public static String PICTURE_DIR;// 相机拍照默认存储路径

    public String DATE = "";// 用于照相时，给文件命名

    private String photo_image;// 照片图片完整名
    private String crop_image;// 截图图片完整名

    private boolean isCrop = true;// 是否裁剪
    private boolean isSquareCropBox = true;// 是否正方形裁剪框
    private int cropWidth;// 裁剪的宽度
    private int cropHeight;// 裁剪的高度

    private Activity activity;

    public TakePhotoUtils(Activity activity) {
        this.activity = activity;
        String packageName = AppUtils.getInstance(activity).mAppStatus.packageName;
        FILE_CONTENT_FILEPROVIDER = packageName + ".fileprovider";
        PICTURE_DIR = StorageUtils.InternalStorageHelper.getCacheDir(activity) + "/pictures/";
    }

    /**
     * 是否裁剪，默认为true
     */
    public TakePhotoUtils setCrop(boolean crop) {
        isCrop = crop;
        return this;
    }

    /**
     * 设置裁剪框宽度
     *
     * @param cropWidth 默认400px
     * @return
     */
    public TakePhotoUtils setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
        return this;
    }

    /**
     * 设置裁剪框高度
     *
     * @param cropHeight 默认600px
     * @return
     */
    public TakePhotoUtils setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
        return this;
    }

    /**
     * 是否使用方形裁剪框，默认为true
     */
    public TakePhotoUtils setSquareCropBox(boolean squareCropBox) {
        isSquareCropBox = squareCropBox;
        return this;
    }

    /***
     * 从相册中取图片
     */
    public void pickPhoto() {
        DATE = new SimpleDateFormat("yyyy_MMdd_hhmmss").format(new Date());
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
    }

    /**
     * 照相
     */
    public void takePhoto() {
        DATE = new SimpleDateFormat("yyyy_MMdd_hhmmss").format(new Date());
        photo_image = createImagePath("_takephoto_" + DATE);
        File file = new File(photo_image);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(activity, FILE_CONTENT_FILEPROVIDER, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        try {
            activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        } catch (ActivityNotFoundException anf) {
            ToastUtilsKt.shortToastCenter(activity, "摄像头尚未准备好");
        }
    }

    /**
     * 裁剪图片
     */
    private void crop(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        crop_image = createImagePath("_crop_" + DATE);

        Uri imageUri;
        Uri outputUri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(activity, FILE_CONTENT_FILEPROVIDER, file);
            outputUri = Uri.fromFile(new File(crop_image));
            //TODO:outputUri不需要ContentUri,否则失败
            //outputUri = FileProvider.getUriForFile(activity, "xxx.xxx.xxx.fileprovider", new File(crop_image));
        } else {
            imageUri = Uri.fromFile(file);
            outputUri = Uri.fromFile(new File(crop_image));
        }

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);// 设置返回的uri
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别功能
        intent.putExtra("return-data", false);// 设置为不返回数据

        if (isSquareCropBox) {
            int width = DEFAULT_CROP_WIDTH;
            if (cropWidth > 0 && cropHeight > 0) {
                width = Math.min(cropWidth, cropHeight);
            } else if (cropWidth > 0) {
                width = cropWidth;
            } else if (cropHeight > 0) {
                width = cropHeight;
            }
            //设置裁剪图片宽高
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", width);
            //设置宽高比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        } else {
            int width = DEFAULT_CROP_WIDTH;
            int height = DEFAULT_CROP_HEIGHT;
            if (cropWidth > 0) {
                width = cropWidth;
            }
            if (cropHeight > 0) {
                height = cropHeight;
            }
            // aspectX aspectY 是宽高的比例，根据自己情况修改
            intent.putExtra("aspectX", width);
            intent.putExtra("aspectY", height);
            // outputX outputY 是裁剪图片宽高像素
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }
        activity.startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
    }

    /**
     * 处理返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO:
                if (!TextUtils.isEmpty(photo_image)) {
                    RxBus.post(RxBusTag.TAG_TAKE_PHOTO_SUCCESS, photo_image);
                    if (isCrop) {
                        crop(photo_image);
                    }
                    photo_image = "";
                } else {
                    ToastUtilsKt.shortToastCenter(activity, "拍照失败");
                }
                break;
            case REQUEST_CODE_SELECT_PICTURE:
                Uri uri;
                String pick_image = null;
                if (data != null) {
                    uri = data.getData();
                    if (uri != null) {
                        pick_image = UriUtils.getPath(activity, uri);
                    }
                }
                if (!TextUtils.isEmpty(pick_image)) {
                    RxBus.post(RxBusTag.TAG_PICK_PHOTO_SUCCESS, pick_image);
                    if (isCrop) {
                        crop(pick_image);
                    }
                } else {
                    ToastUtilsKt.shortToastCenter(activity, "图片选择失败");
                }
                break;
            case REQUEST_CODE_CROP_PICTURE:
                if (!TextUtils.isEmpty(crop_image)) {
                    RxBus.post(RxBusTag.TAG_CROP_PHOTO_SUCCESS, crop_image);
                    crop_image = "";
                } else {
                    ToastUtilsKt.shortToastCenter(activity, "裁剪失败");
                }
                break;
        }
    }

    /**
     * 创建图片的存储路径
     */
    private static String createImagePath(String imageName) {
        String dir = PICTURE_DIR;
        File destDir = new File(dir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = null;
        if (!TextUtils.isEmpty(imageName)) {
            file = new File(dir, imageName + ".jpeg");
        }
        return file.getAbsolutePath();
    }
}