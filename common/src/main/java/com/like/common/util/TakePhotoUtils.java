package com.like.common.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;

import com.like.rxbus.RxBus;
import com.like.toast.ToastUtils;

import java.io.File;
import java.io.IOException;

/**
 * 照相相关工具类，可以照相、从相册获取图片、裁剪。
 * <p>
 * {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
 *
 * @author like
 * @version 1.0
 *          created on 2017/4/19 9:24
 */
public class TakePhotoUtils {
    //使用照相机拍照获取图片
    public static final int CODE_TAKE_PHOTO = 1;
    //使用相册中的图片
    public static final int CODE_PICK_PHOTO = 2;
    //图片裁剪
    private static final int CODE_CROP_PHOTO = 3;

    private Uri mPhotoUri;
    private File cropFile;// 单独存放裁剪后的图片，避免把相册中选择的原图片裁剪了。

    private boolean isCrop;
    // 是否正方形裁剪框
    private boolean isSquareCropBox;
    private int cropWidth;
    private int cropHeight;

    private Activity activity;
    private volatile static TakePhotoUtils sInstance = null;

    public static TakePhotoUtils getInstance(Activity activity) {
        if (sInstance == null) {
            synchronized (TakePhotoUtils.class) {
                if (sInstance == null) {
                    sInstance = new TakePhotoUtils(activity);
                }
            }
        }
        return sInstance;
    }

    private TakePhotoUtils(Activity activity) {
        this.activity = activity;
        cropFile = new File(StorageUtils.ExternalStorageHelper.getPrivateCacheDir(activity), "photo_temp.jpg");
        if (!cropFile.exists()) {
            try {
                cropFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否裁剪
     *
     * @param crop
     * @return
     */
    public TakePhotoUtils setCrop(boolean crop) {
        isCrop = crop;
        return sInstance;
    }

    /**
     * 设置裁剪框宽度
     *
     * @param cropWidth 默认400px
     * @return
     */
    public TakePhotoUtils setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
        return sInstance;
    }

    /**
     * 设置裁剪框高度
     *
     * @param cropHeight 默认600px
     * @return
     */
    public TakePhotoUtils setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
        return sInstance;
    }

    /**
     * 是否使用方形裁剪框
     *
     * @param squareCropBox
     * @return
     */
    public TakePhotoUtils setSquareCropBox(boolean squareCropBox) {
        isSquareCropBox = squareCropBox;
        return sInstance;
    }

    /***
     * 从相册中取图片
     */
    public void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, CODE_PICK_PHOTO);
    }

    /**
     * 照相，拍照后把照片存储在数据库中
     *
     * @return mPhotoUri，可以根据此值来从数据库中查询照片
     */
    public void takePhoto() {
        //向 MediaStore.Images.Media.EXTERNAL_CONTENT_URI 插入一个数据，那么返回标识ID。
        //在完成拍照后，新的照片会以此处的photoUri命名. 其实就是指定了个文件名
        mPhotoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        //准备intent，并指定新照片的文件名（mPhotoUri）
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        //启动拍照的窗体。并注册回调处理。
        activity.startActivityForResult(intent, CODE_TAKE_PHOTO);
    }

    /**
     * 处理返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CODE_TAKE_PHOTO:
                    handleTakePhotoResult();
                    break;
                case CODE_PICK_PHOTO:
                    handlePickPhotoResult(data);
                    break;
                case CODE_CROP_PHOTO:
                    handleCropPhotoResult();
                    break;
            }
        }
    }

    /**
     * 处理裁剪返回的结果
     */
    private void handleCropPhotoResult() {
        RxBus.post(RxBusTag.TAG_CROP_PHOTO_SUCCESS, BitmapFactory.decodeFile(cropFile.getAbsolutePath()));
    }

    /**
     * 处理相册返回的结果
     *
     * @param data
     */
    private void handlePickPhotoResult(Intent data) {
        if (null != data && null != data.getData()) {
            Uri uri = data.getData();
            RxBus.post(RxBusTag.TAG_PICK_PHOTO_SUCCESS, getBitmapFromUri(uri));
            if (isCrop) {
                startPhotoZoom(uri, CODE_CROP_PHOTO);
            }
        } else {
            ToastUtils.showShortCenter(activity, "图片选择失败");
        }
    }

    /**
     * @param
     * @description 裁剪图片
     * @author ldm
     * @time 2016/11/30 15:19
     */
    private void startPhotoZoom(Uri uri, int REQUE_CODE_CROP) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        if (isSquareCropBox) {
            // aspectX aspectY 是宽高的比例，根据自己情况修改
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高像素
            int width = 400;
            if (cropWidth > 0 && cropHeight > 0) {
                width = Math.min(cropWidth, cropHeight);
            } else if (cropWidth > 0) {
                width = cropWidth;
            } else if (cropHeight > 0) {
                width = cropHeight;
            }
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", width);
        } else {
            int width = 600;
            int height = 400;
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
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        //设置返回的uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        //设置为不返回数据
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, REQUE_CODE_CROP);
    }


    /**
     * 处理照相返回的结果
     */
    private void handleTakePhotoResult() {
        RxBus.post(RxBusTag.TAG_TAKE_PHOTO_SUCCESS, getBitmapFromUri(mPhotoUri));
        if (isCrop) {
            startPhotoZoom(mPhotoUri, CODE_CROP_PHOTO);
        }
        mPhotoUri = null;
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        if (uri == null)
            return null;

        Bitmap bitmap = null;
        ContentResolver cr = activity.getContentResolver();
        //按 刚刚指定 的那个文件名，查询数据库，获得更多的 照片信息，比如 图片的物理绝对路径
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String path = cursor.getString(1);
                //获得图片
                bitmap = getBitMapFromPath(path);
            }
        }
        CloseableUtils.close(cursor);
        return bitmap;
    }

    /* 获得图片，并进行适当的 缩放。 图片太大的话，是无法展示的。 */
    private Bitmap getBitMapFromPath(String imageFilePath) {
        Display currentDisplay = activity.getWindowManager().getDefaultDisplay();
        int dw = currentDisplay.getWidth();
        int dh = currentDisplay.getHeight();
        // Load up the image's dimensions not the image itself
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) dh);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) dw);

        // If both of the ratios are greater than 1,
        // one of the sides of the image is greater than the screen
        if (heightRatio > 1 && widthRatio > 1) {
            if (heightRatio > widthRatio) {
                // Height ratio is larger, scale according to it
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                // Width ratio is larger, scale according to it
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }
        // Decode it for real
        bmpFactoryOptions.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
        return bmp;
    }

}