package com.like.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 使用Glide框架实现的图片加载工具类
 */
public class ImageLoaderUtils {
    private Context mContext;

    private volatile static ImageLoaderUtils sInstance = null;

    public static ImageLoaderUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoaderUtils(context);
                }
            }
        }
        return sInstance;
    }

    private ImageLoaderUtils(Context context) {
        mContext = context;
    }

    /**
     * 下载监听器
     */
    public interface DownloadListener {
        void onCompleted(List<ImageDownLoadInfo> imageDownLoadInfoList);
    }

    public class ImageDownLoadInfo {
        public String url;
        public Bitmap bitmap;

        public ImageDownLoadInfo(String url, Bitmap bitmap) {
            this.url = url;
            this.bitmap = bitmap;
        }
    }

    /**
     * 批量下载图片
     *
     * @param urlList
     */
    public Observable<ImageDownLoadInfo> downloadImages(List<String> urlList) {
        List<ImageDownLoadInfo> result = new ArrayList<>();
        return Observable.fromIterable(urlList)
                .flatMap(s -> {
                    Observable<String> urlObservable = Observable.just(s);
                    Observable<Bitmap> bitmapObservable = downloadImage(s);
                    return Observable.zip(urlObservable, bitmapObservable, (s1, bitmap) -> new ImageDownLoadInfo(s1, bitmap));
                });
    }

    /**
     * 下载图片
     *
     * @param url
     */
    public Observable<Bitmap> downloadImage(String url) {
        return Observable.create(observableEmitter -> {
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)// 跳过硬盘缓存
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                observableEmitter.onNext(bitmap);
                observableEmitter.onComplete();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                observableEmitter.onError(e);
            }
        });
    }

    public void displayRoundRect(String url, ImageView imageView, int radius) {
        displayRoundRect(url, imageView, radius, -1);
    }

    public void displayRoundRect(String url, ImageView imageView, int radius, int loadingImageResId) {
        displayRoundRect(url, imageView, radius, loadingImageResId, -1);
    }

    /**
     * 显示圆角矩形图片
     *
     * @param url                 图片地址
     * @param imageView
     * @param radius              圆角半径，单位dp
     * @param loadingImageResId   加载中的图片
     * @param loadErrorImageResId 加载失败的图片
     */
    public void displayRoundRect(String url, ImageView imageView, int radius, int loadingImageResId, int loadErrorImageResId) {
        Glide.with(mContext)
                .load(url)
                .placeholder(loadingImageResId)
                .error(loadErrorImageResId)
                .fitCenter()// 缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个ImageView。
                .skipMemoryCache(true)// 跳过内存缓存
                .priority(Priority.HIGH)// 优先级，设置图片加载的顺序
                .diskCacheStrategy(DiskCacheStrategy.NONE)// 跳过硬盘缓存
                .bitmapTransform(new RoundedCornersTransformation(mContext, radius, 0))
                .into(imageView);
    }

    public void displayCircle(String url, ImageView imageView) {
        displayCircle(url, imageView, -1);
    }

    public void displayCircle(String url, ImageView imageView, int loadingImageResId) {
        displayCircle(url, imageView, loadingImageResId, -1);
    }

    /**
     * 显示圆形图片
     *
     * @param url                 图片地址
     * @param imageView
     * @param loadingImageResId   加载中的图片
     * @param loadErrorImageResId 加载失败的图片
     */
    public void displayCircle(String url, ImageView imageView, int loadingImageResId, int loadErrorImageResId) {
        Glide.with(mContext)
                .load(url)
                .placeholder(loadingImageResId)
                .error(loadErrorImageResId)
                .fitCenter()// 缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个ImageView。
                .skipMemoryCache(true)// 跳过内存缓存
                .priority(Priority.HIGH)// 优先级，设置图片加载的顺序
                .diskCacheStrategy(DiskCacheStrategy.NONE)// 跳过硬盘缓存
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(imageView);
    }

    public void display(String url, ImageView imageView) {
        display(url, imageView, -1);
    }

    public void display(String url, ImageView imageView, int loadingImageResId) {
        display(url, imageView, loadingImageResId, -1);
    }

    /**
     * 显示图片
     *
     * @param url                 图片地址
     * @param imageView
     * @param loadingImageResId   加载中的图片
     * @param loadErrorImageResId 加载失败的图片
     */
    public void display(String url, ImageView imageView, int loadingImageResId, int loadErrorImageResId) {
        Glide.with(mContext)
                .load(url)
                .placeholder(loadingImageResId)
                .error(loadErrorImageResId)
                .fitCenter()// 缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个ImageView。
                .priority(Priority.HIGH)// 优先级，设置图片加载的顺序
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)// 跳过硬盘缓存
                .into(imageView);
    }

}
