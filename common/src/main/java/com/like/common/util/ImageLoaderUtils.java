package com.like.common.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    private RequestManager requestManager;

    public ImageLoaderUtils(Context context) {
        requestManager = Glide.with(context);
        mContext = context;
    }

    public ImageLoaderUtils(Fragment fragment) {
        requestManager = Glide.with(fragment);
        mContext = fragment.getActivity();
    }

    public ImageLoaderUtils(android.support.v4.app.Fragment fragment) {
        requestManager = Glide.with(fragment);
        mContext = fragment.getActivity();
    }

    public ImageLoaderUtils(Activity activity) {
        requestManager = Glide.with(activity);
        mContext = activity;
    }

    public ImageLoaderUtils(FragmentActivity activity) {
        requestManager = Glide.with(activity);
        mContext = activity;
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
                Bitmap bitmap = requestManager
                        .load(url)
                        .asBitmap()
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
        requestManager
                .load(url)
                .placeholder(loadingImageResId)
                .error(loadErrorImageResId)
                .fitCenter()// 缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个ImageView。
                .priority(Priority.HIGH)// 优先级，设置图片加载的顺序
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)// 跳过硬盘缓存
                .bitmapTransform(new RoundedCornersTransformation(mContext, radius, 0))
                .into(imageView);
    }

    public void displayCircle(String string, ImageView imageView) {
        displayCircle(string, imageView, -1);
    }

    public void displayCircle(String string, ImageView imageView, int loadingImageResId) {
        displayCircle(string, imageView, loadingImageResId, -1);
    }

    /**
     * 显示圆形图片
     *
     * @param string              A file path, or a uri or url handled by {@link com.bumptech.glide.load.model.UriLoader}.
     * @param imageView
     * @param loadingImageResId   加载中的图片
     * @param loadErrorImageResId 加载失败的图片
     */
    public void displayCircle(String string, ImageView imageView, int loadingImageResId, int loadErrorImageResId) {
        requestManager
                .load(string)
                .placeholder(loadingImageResId)
                .error(loadErrorImageResId)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(imageView);
    }

    public void display(String url, ImageView imageView) {
        display(url, imageView, -1);
    }

    public void display(String url, ImageView imageView, RequestListener listener) {
        display(url, imageView, -1, -1, listener);
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
        display(url, imageView, loadingImageResId, loadErrorImageResId, null);
    }

    public void display(String url, ImageView imageView, int loadingImageResId, int loadErrorImageResId, RequestListener listener) {
        DrawableRequestBuilder<String> builder = requestManager
                .load(url)
                .placeholder(loadingImageResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(loadErrorImageResId);
        if (listener != null)
            builder.listener(listener);
        builder.into(imageView);
    }

    public void isCached(String url, int width, int height, CheckCachedListener listener) {
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {
                File cacheFile = null;
                if (!url.isEmpty()) {
                    try {
                        FutureTarget<File> future = requestManager
                                .using(new StreamModelLoader<String>() {
                                    @Override
                                    public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
                                        return new DataFetcher<InputStream>() {
                                            @Override
                                            public InputStream loadData(Priority priority) throws Exception {
                                                throw new IOException();
                                            }

                                            @Override
                                            public void cleanup() {

                                            }

                                            @Override
                                            public String getId() {
                                                return model;
                                            }

                                            @Override
                                            public void cancel() {

                                            }
                                        };
                                    }
                                })
                                .load(url)
                                .downloadOnly(width, height);
                        cacheFile = future.get();
                    } catch (Exception ex) {
                        ex.printStackTrace();  //exception thrown if image not in cache
                    }
                }
                return cacheFile;
            }

            @Override
            protected void onPostExecute(File file) {
                if (listener != null) {
                    listener.onChecked(file != null && file.length() > 0);
                }
            }
        }.execute();
    }

    public interface CheckCachedListener {
        void onChecked(boolean isCached);
    }

}
