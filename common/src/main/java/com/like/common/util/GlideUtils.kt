package com.like.common.util

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.like.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class GlideUtils {
    private var mContext: Context
    private var glideRequests: GlideRequests

    constructor(context: Context) {
        glideRequests = GlideApp.with(context)
        mContext = context
    }

    constructor(fragment: Fragment) {
        glideRequests = GlideApp.with(fragment)
        mContext = fragment.activity
    }

    constructor(fragment: android.support.v4.app.Fragment) {
        glideRequests = GlideApp.with(fragment)
        mContext = fragment.activity
    }

    constructor(activity: Activity) {
        glideRequests = GlideApp.with(activity)
        mContext = activity
    }

    constructor(activity: android.support.v4.app.FragmentActivity) {
        glideRequests = GlideApp.with(activity)
        mContext = activity
    }

    interface onGetSizeListener {
        fun getSize(bitmap: Bitmap?, width: Int, height: Int)
    }

    fun getBitmapSize(string: String, listener: onGetSizeListener) {
        glideRequests.asBitmap().load(string).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                listener.getSize(resource, resource?.width ?: 0, resource?.height ?: 0)
            }
        })
    }

    /**
     * 显示高斯模糊图片
     */
    @JvmOverloads
    fun displayBlur(string: String, imageView: ImageView, radius: Int, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.transform(BlurTransformation(radius))?.into(imageView)
    }

    /**
     * 显示圆角矩形图片
     */
    @JvmOverloads
    fun displayRoundRect(string: String, imageView: ImageView, radius: Int, cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.transform(RoundedCornersTransformation(radius, 0, cornerType))?.into(imageView)
    }

    /**
     * 显示圆形图片
     */
    @JvmOverloads
    fun displayCircle(string: String, imageView: ImageView, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.circleCrop()?.into(imageView)
    }

    /**
     * 显示图片
     */
    @JvmOverloads
    fun display(string: String, imageView: ImageView, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.into(imageView)
    }

    /**
     * 显示高斯模糊图片
     */
    @JvmOverloads
    fun displayBlurNoCache(string: String, imageView: ImageView, radius: Int, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, diskCacheStrategy = DiskCacheStrategy.NONE, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.transform(BlurTransformation(radius))?.into(imageView)
    }

    /**
     * 显示圆角矩形图片
     */
    @JvmOverloads
    fun displayRoundRectNoCache(string: String, imageView: ImageView, radius: Int, cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, diskCacheStrategy = DiskCacheStrategy.NONE, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.transform(RoundedCornersTransformation(radius, 0, cornerType))?.into(imageView)
    }

    /**
     * 显示圆形图片
     */
    @JvmOverloads
    fun displayCircleNoCache(string: String, imageView: ImageView, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, diskCacheStrategy = DiskCacheStrategy.NONE, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.circleCrop()?.into(imageView)
    }

    /**
     * 显示图片
     */
    @JvmOverloads
    fun displayNoCache(string: String, imageView: ImageView, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null) {
        createGlideRequest(string, diskCacheStrategy = DiskCacheStrategy.NONE, loadingImageResId = loadingImageResId, loadErrorImageResId = loadErrorImageResId, listener = listener)?.into(imageView)
    }

    /**
     * 显示图片
     *
     * @param string                A file path, or a uri or url handled by [com.bumptech.glide.load.model.UriLoader].
     * @param diskCacheStrategy     硬盘缓存策略，默认DiskCacheStrategy.ALL
     * @param loadingImageResId     加载中的图片
     * @param loadErrorImageResId   加载失败的图片
     * @param listener              加载成功失败监听
     */
    private fun createGlideRequest(string: String, diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.ALL, loadingImageResId: Int = -1, loadErrorImageResId: Int = -1, listener: DisplayListener? = null): GlideRequest<Drawable>? {
        val glideRequest = glideRequests
                .load(string)
                .diskCacheStrategy(diskCacheStrategy)
        if (diskCacheStrategy == DiskCacheStrategy.NONE) {
            glideRequest.skipMemoryCache(true).signature(ObjectKey(System.currentTimeMillis()))
        }
        if (loadingImageResId > 0) {
            glideRequest.placeholder(loadingImageResId)
        }
        if (loadErrorImageResId > 0) {
            glideRequest.error(loadErrorImageResId)
        }
        listener?.let {
            glideRequest.listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    it.onFailure()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    it.onSuccess()
                    return false
                }
            })
        }
        return glideRequest
    }

    /**
     * 指定图片是否被Glide缓存了
     */
    @JvmOverloads
    fun isCached(string: String, width: Int = Target.SIZE_ORIGINAL, height: Int = Target.SIZE_ORIGINAL, listener: CheckCachedListener? = null) {
        glideRequests.load(string)
                .onlyRetrieveFromCache(true)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        Logger.e("没有缓存：$string")
                        listener?.onChecked(false)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        Logger.w("有缓存：$string")
                        listener?.onChecked(true)
                        return false
                    }
                })
                .submit(width, height)
    }

    /**
     * 批量下载图片
     *
     * @param urlList
     */
    fun downloadImages(urlList: List<String>): Observable<android.support.v4.util.Pair<String, Bitmap>> {
        return Observable.fromIterable(urlList)
                .flatMap<android.support.v4.util.Pair<String, Bitmap>> { s ->
                    val urlObservable = Observable.just(s)
                    val bitmapObservable = downloadImage(s)
                    val observable = Observable.zip<String, Bitmap, android.support.v4.util.Pair<String, Bitmap>>(urlObservable, bitmapObservable, io.reactivex.functions.BiFunction({ t1, t2 -> android.support.v4.util.Pair(t1, t2) }))
                    observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                }
    }

    /**
     * 下载图片
     *
     * @param url
     */
    fun downloadImage(url: String): Observable<Bitmap> {
        val observable: Observable<Bitmap> = Observable.create { observableEmitter ->
            try {
                val bitmap = glideRequests
                        .asBitmap()
                        .load(url)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                observableEmitter.onNext(bitmap)
                observableEmitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                observableEmitter.onError(e)
            }
        }
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 检查缓存监听
     */
    interface CheckCachedListener {
        fun onChecked(isCached: Boolean)
    }

    /**
     * 显示图片监听
     */
    interface DisplayListener {
        fun onSuccess()
        fun onFailure()
    }
}
