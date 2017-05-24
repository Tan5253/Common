package com.like.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;

import com.like.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by like on 2016/12/6.
 */

public class ImageUtils {
    /**
     * 转换成圆角图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 转换成带倒影的图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = createBitmap(w, (h + h / 2),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 将Bitmap转化为Drawable
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 质量压缩。（不会减小宽高和大小，及不改变在手机中占用的内存，只是改变了本地存储的大小，适用于传递二进制图片数据)
     * 不会减少图片的像素，它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的。
     * 图片的长，宽，像素都不变，所占内存大小也不会变的。
     * 但是bytes.length是随着quality变小而变小，这样适合去传递二进制的图片数据。
     * 注意：此方法是通过修改图片的其它比如透明度等属性，使得图片大小变化而已，所以它就无法无限压缩，到达一个值之后就不会继续变小了。
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomBitmapByQuality(Bitmap bitmap, int maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        Logger.w("原图宽高：" + bitmap.getWidth() + "X" + bitmap.getHeight() + "，大小：" + ImageUtils.getBitmapSizeKB(bitmap) + "KB");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 注意：这里不能设置为CompressFormat.PNG，因为png图片是无损的，不能进行压缩。bytes.length不会变化。

        // 循环判断压缩后图片是否超过限制大小
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 清空baos
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            if (quality == 0) {
                break;
            }
            quality -= 10;
        }

        Bitmap zoomBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
        if (quality == 0 && baos.toByteArray().length / 1024 > maxSize) {
            // 当质量压缩到了极限，就用其它压缩方式压缩
            zoomBitmap = getZoomBitmapByCreateScaledBitmap(zoomBitmap, maxSize);
        }
        Logger.w("缩略图宽高：" + zoomBitmap.getWidth() + "X" + zoomBitmap.getHeight() + "，大小：" + ImageUtils.getBitmapSizeKB(zoomBitmap) + "KB，quality：" + quality);
        return zoomBitmap;
    }

    /**
     * 采样率压缩（会减小宽高和大小）
     *
     * @param imgPath
     * @param reqWidth  px
     * @param reqHeight px
     * @return
     */
    public static Bitmap getZoomBitmapByOptions(String imgPath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        options.inJustDecodeBounds = true;
        Bitmap zoomBitmap = BitmapFactory.decodeFile(imgPath, options);
        Logger.w("原图宽高：" + options.outWidth + "X" + options.outHeight);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例
        options.inJustDecodeBounds = false;
        // 由于得到的图片的宽或者高会比期望值大一点，所以再进行缩放。
        // 最后一个参数filter：如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        zoomBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgPath, options), reqWidth, reqHeight, true);
        Logger.w("缩略图宽高：" + zoomBitmap.getWidth() + "X" + zoomBitmap.getHeight() + "，大小：" + ImageUtils.getBitmapSizeKB(zoomBitmap) + "KB");
        return zoomBitmap;
    }

    /**
     * 计算缩放比例
     *
     * @param options   参数
     * @param reqWidth  目标的宽度 px
     * @param reqHeight 目标的高度 px
     * @return
     * @description 计算图片的压缩比率
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * createScaledBitmap压缩（会减小宽高和大小）
     *
     * @param bitmap
     * @param maxSize KB
     * @return
     */
    public static Bitmap getZoomBitmapByCreateScaledBitmap(Bitmap bitmap, int maxSize) {
        float r = (float) (bitmap.getHeight()) / (float) (bitmap.getWidth());
        int newWidth = (int) Math.sqrt((float) (maxSize * 1024) / r);
        int newHeight = (int) (newWidth * r);
        return getZoomBitmapByCreateScaledBitmap(bitmap, newWidth, newHeight);
    }

    /**
     * createScaledBitmap压缩（会减小宽高和大小）
     *
     * @param bitmap
     * @param reqWidth  目标的宽度 px
     * @param reqHeight 目标的高度 px
     * @return
     */
    public static Bitmap getZoomBitmapByCreateScaledBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        Logger.w("原图宽高：" + bitmap.getWidth() + "X" + bitmap.getHeight() + "，大小：" + ImageUtils.getBitmapSizeKB(bitmap) + "KB");
        Bitmap zoomBitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
        Logger.w("缩略图宽高：" + zoomBitmap.getWidth() + "X" + zoomBitmap.getHeight() + "，大小：" + ImageUtils.getBitmapSizeKB(zoomBitmap) + "KB");
        return zoomBitmap;
    }

    /**
     * 缩放压缩（会减小宽高和大小）
     *
     * @param bitmap
     * @param maxSize
     * @return
     */
    public static Bitmap getZoomBitmapByScale(Bitmap bitmap, int maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        Logger.w("原图宽高：" + bitmap.getWidth() + "X" + bitmap.getHeight() + "，大小：" + ImageUtils.getBitmapSizeKB(bitmap) + "KB");
        // 单位：从 Byte 换算成 KB
        double currentSize = getBitmapSizeKB(bitmap);
        Bitmap zoomBitmap = bitmap;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            int width = (int) (bitmap.getWidth() / Math.sqrt(multiple));
            int height = (int) (bitmap.getHeight() / Math.sqrt(multiple));

            zoomBitmap = getZoomBitmapByScale(zoomBitmap, width, height);
            currentSize = getBitmapSizeKB(zoomBitmap);
        }
        Logger.w("缩略图宽高：" + zoomBitmap.getWidth() + "X" + zoomBitmap.getHeight() + "，大小：" + getBitmapSizeKB(zoomBitmap) + "KB");
        return zoomBitmap;
    }

    /**
     * 缩放压缩（会减小宽高和大小）
     *
     * @param bitmap
     * @param reqWidth  目标的宽度 px
     * @param reqHeight 目标的高度 px
     * @return
     */
    public static Bitmap getZoomBitmapByScale(Bitmap bitmap, int reqWidth, int reqHeight) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }
        if (reqWidth <= 0 || reqHeight <= 0) {
            return bitmap;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Logger.w("原图宽高：" + w + "X" + h + "，大小：" + ImageUtils.getBitmapSizeKB(bitmap) + "KB");
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) reqWidth / w);
        float scaleHeight = ((float) reqHeight / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap zoomBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        Logger.w("缩略图宽高：" + zoomBitmap.getWidth() + "X" + zoomBitmap.getHeight() + "，大小：" + getBitmapSizeKB(zoomBitmap) + "KB");
        return zoomBitmap;
    }

    public static int getBitmapSizeMB(Bitmap bitmap) {
        return getBitmapSize(bitmap) / 1024 / 1024;
    }

    public static int getBitmapSizeKB(Bitmap bitmap) {
        return getBitmapSize(bitmap) / 1024;
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (null == bitmap) {
            return 0;
        }
        if (bitmap.isRecycled()) {
            return 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * 从资源文件中获取Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitmapFromResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    /**
     * Byte[]转换到Bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    /**
     * Bitmap转换到Byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        byte[] bitmapByte = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将bitmap放入字节数组流中
            bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
            bos.close();
            bitmapByte = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(bos);
        }
        return bitmapByte;
    }

    /**
     * Base64后的image数据转换成byte[]
     *
     * @param imageBase64String
     * @return
     */
    public static byte[] string2Bytes(String imageBase64String) {
        return Base64.decode(imageBase64String, Base64.DEFAULT);
    }

    /**
     * Base64后的image数据转换成Bitmap
     *
     * @param imageBase64String
     * @return
     */
    public static Bitmap string2Bitmap(String imageBase64String) {
        byte[] bytes = string2Bytes(imageBase64String);
        return bytes2Bitmap(bytes);
    }

    /**
     * byte[]转换成Base64字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmap2String(Bitmap bitmap) {
        return Base64.encodeToString(bitmap2Bytes(bitmap), Base64.DEFAULT);
    }

    /**
     * byte[]转换成Base64字符串
     *
     * @param imageData
     * @return
     */
    public static String bytes2String(byte[] imageData) {
        return Base64.encodeToString(imageData, Base64.DEFAULT);
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 1);
        int height = Math.max(drawable.getIntrinsicHeight(), 1);
        try {
            bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }

    /**
     * Get bitmap from specified image path
     *
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {
        return BitmapFactory.decodeFile(imgPath);
    }

    /**
     * Store bitmap into specified image path
     *
     * @param bitmap
     * @param outPath
     * @throws FileNotFoundException
     */
    public static void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(outPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
    }

    /**
     * Store bitmap into specified image path
     *
     * @param data
     * @param outPath
     * @throws FileNotFoundException
     */
    public static void storeImage(byte[] data, String outPath) throws FileNotFoundException {
        Bitmap bitmap = bytes2Bitmap(data);
        if (bitmap != null) {
            storeImage(bitmap, outPath);
        }
    }


}
