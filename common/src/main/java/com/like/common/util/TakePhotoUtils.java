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

/**
 * 照相相关工具类
 * <p>
 * {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
 *
 * @author like
 * @version 1.0
 *          created on 2017/4/19 9:24
 */
public class TakePhotoUtils {
    private Uri mPhotoUri;
    private int mRequestCode;

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
    }

    /**
     * 打开照相机，拍照后把照片存储在数据库中
     *
     * @param requestCode
     * @return mPhotoUri，可以根据此值来从数据库中查询照片
     */
    public void openCamera(int requestCode) {
        mRequestCode = requestCode;
        //向 MediaStore.Images.Media.EXTERNAL_CONTENT_URI 插入一个数据，那么返回标识ID。
        //在完成拍照后，新的照片会以此处的photoUri命名. 其实就是指定了个文件名
        mPhotoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        //准备intent，并指定新照片的文件名（mPhotoUri）
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        //启动拍照的窗体。并注册回调处理。
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 处理照相返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleTakePhotoResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode && resultCode == Activity.RESULT_OK) {
            if (mPhotoUri == null)
                return;
            ContentResolver cr = activity.getContentResolver();
            //按 刚刚指定 的那个文件名，查询数据库，获得更多的 照片信息，比如 图片的物理绝对路径
            Cursor cursor = cr.query(mPhotoUri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    String path = cursor.getString(1);
                    //获得图片
                    Bitmap bitmap = getBitMapFromPath(path);
                    TakePhotoResult takePhotoResult = new TakePhotoResult();
                    takePhotoResult.requestCode = requestCode;
                    takePhotoResult.resultCode = resultCode;
                    takePhotoResult.data = data;
                    takePhotoResult.photoUri = mPhotoUri;
                    takePhotoResult.bitmap = bitmap;
                    RxBus.post(RxBusTag.TAG_TAKE_PHOTO_SUCCESS, takePhotoResult);
                }
            }
            CloseableUtils.close(cursor);
            mPhotoUri = null;
        }
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
        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                / (float) dh);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                / (float) dw);

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

    public class TakePhotoResult {
        public int requestCode;
        public int resultCode;
        public Intent data;
        public Uri photoUri;
        public Bitmap bitmap;
    }

}