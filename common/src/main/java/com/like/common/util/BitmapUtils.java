package com.like.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;

import com.like.logger.Logger;
import com.like.toast.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * bitmap相关工具类
 */
public class BitmapUtils {

    /**
     * 压缩图片大小和质量
     *
     * @param context
     * @param imagePath
     * @param targetSize
     * @return 缩略图路径。/storage/emulated/0/Android/data/com.bbmycare.o2o/cache/
     * 1464411392396.jpg
     */
    public static String compressAndSaveImage(Context context, String imagePath, int targetSize) {
        Context applicationContext = context.getApplicationContext();
        File file = new File(imagePath);
        if (!file.exists()) {
            ToastUtils.showShortCenter(applicationContext, "图片不存在");
            return null;
        }
        Bitmap thumbnail = BitmapUtils.getNormalDegreeThumbnail(imagePath);
        if (thumbnail == null) {
            ToastUtils.showShortCenter(applicationContext, "压缩图片大小失败");
            return null;
        }
        String targetPath = applicationContext.getExternalCacheDir() + file.getName();
        if (!BitmapUtils.compressBmpToFile(thumbnail, targetPath, targetSize)) {
            ToastUtils.showShortCenter(applicationContext, "压缩图片质量失败");
            return null;
        }
        return targetPath;
    }

    /**
     * 压缩并保存图片到本地
     *
     * @param bmp
     * @param targetPath
     * @param targetSize 目标文件大小，kb
     */
    public static boolean compressBmpToFile(Bitmap bmp, String targetPath, int targetSize) {
        boolean result = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        bmp.compress(CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > targetSize) {
            baos.reset();
            options -= 10;
            bmp.compress(CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(targetPath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取正常角度的缩略图片
     *
     * @param imagePath
     * @return
     */
    private static Bitmap getNormalDegreeThumbnail(String imagePath) {
        return getNormalDegreeThumbnail(imagePath, 240, 400);
    }

    /**
     * 获取正常角度的缩略图片(480*800)
     *
     * @param imagePath 原始图片路径
     * @param reqWidth  缩略后的宽
     * @param reqHeight 缩略后的高
     * @return
     */
    private static Bitmap getNormalDegreeThumbnail(String imagePath, int reqWidth, int reqHeight) {
        Bitmap bitmap = getThumbnail(imagePath, reqWidth, reqHeight);
        if (bitmap == null)
            return null;
        int degree = readPictureDegree(imagePath);
        if (degree != 0) {
            // 旋转图片 动作
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            // 创建新的图片
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        Logger.d("缩略图大小：" + (bitmap.getByteCount() / 1024) + " kb");
        return bitmap;
    }

    /**
     * 从本地指定图片路径获取该图片的缩略图
     *
     * @param imagePath 原始图片路径
     * @param reqWidth  缩略后的宽
     * @param reqHeight 缩略后的高
     * @return
     */
    private static Bitmap getThumbnail(String imagePath, int reqWidth, int reqHeight) {
        if (TextUtils.isEmpty(imagePath)) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 获取图片旋转角度
     *
     * @param imagePath
     * @return degree
     */
    private static int readPictureDegree(String imagePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;

    }

    /**
     * 将Bitmap转换成Base64字符串
     *
     * @param imagePath
     * @return
     */
    public static String GetBase64ImageStr(String imagePath) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BitmapFactory.decodeFile(imagePath).compress(CompressFormat.JPEG, 100, bos);
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
