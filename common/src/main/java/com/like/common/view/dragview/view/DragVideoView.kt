package com.like.common.view.dragview.view

import android.R
import android.content.Context
import android.os.AsyncTask
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import com.like.common.util.GlideUtils
import com.like.common.util.StorageUtils
import com.like.common.view.dragview.entity.DragInfo
import com.like.logger.Logger
import java.io.File
import java.net.URL

class DragVideoView(context: Context, info: DragInfo) : BaseDragView(context, info) {

    init {
        val imageView = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        addView(imageView)

        val progressBar = ProgressBar(context, null, R.attr.progressBarStyleInverse).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                addRule(CENTER_IN_PARENT)
            }
        }
        addView(progressBar)

        if (info.thumbImageUrl.isNotEmpty()) {
            mGlideUtils.display(info.thumbImageUrl, imageView, listener = object : GlideUtils.DisplayListener {
                override fun onSuccess() {
                    if (info.videoUrl.isNotEmpty()) {
                        addView(VideoView(context).apply {
                            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                                addRule(CENTER_IN_PARENT)
                            }
                            setZOrderOnTop(true)// 避免闪屏

                            downloadFile(info.videoUrl, object : DownLoadListener {
                                override fun onSuccess(filePath: String) {
                                    setVideoPath(filePath)
                                    setOnPreparedListener { mediaPlayer ->
                                        try {
                                            mediaPlayer?.let {
                                                mediaPlayer.isLooping = true
                                                mediaPlayer.start()
                                                postDelayed({
                                                    removeView(imageView)
                                                    removeView(progressBar)
                                                }, 100)// 防闪烁
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                    setOnErrorListener { _, _, _ ->
                                        removeView(progressBar)
                                        removeView(this@apply)
                                        Toast.makeText(context, "获取视频数据失败！", Toast.LENGTH_SHORT).show()
                                        true
                                    }
                                }

                                override fun onFailure() {
                                    removeView(progressBar)
                                    removeView(this@apply)
                                    Toast.makeText(context, "获取视频数据失败！", Toast.LENGTH_SHORT).show()
                                }
                            })
                        })
                    }
                }

                override fun onFailure() {
                    removeView(progressBar)
                    Toast.makeText(context, "获取视频数据失败！", Toast.LENGTH_SHORT).show()
                }
            })
        }

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                enter()
            }
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // 当scale == 1时才能drag
        if (scaleX == 1f && scaleY == 1f) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    onActionDown(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    // 单手指按下，并在Y方向上拖动了一段距离
                    if (event.pointerCount == 1) {
                        mConfig.updateCanvasTranslationX(event.x - mDownX)
                        mConfig.updateCanvasTranslationY(event.y - mDownY)
                        mConfig.updateCanvasScale()
                        mConfig.updateCanvasBgAlpha()
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    onActionUp(event)
                }
            }
        }
        return true
    }

    private fun downloadFile(fileURL: String, downLoadListener: DownLoadListener) {
        object : AsyncTask<Unit, Unit, String>() {
            override fun doInBackground(vararg p0: Unit?): String {
                try {
                    val rootDir = "${StorageUtils.InternalStorageHelper.getBaseDir(context)}${File.separator}Video"
                    val rootFile = File(rootDir)
                    if (!rootFile.exists()) {
                        rootFile.mkdir()
                    }
                    val file = File(rootFile, fileURL.split("/").last())
                    if (!file.exists()) {
                        file.writeBytes(URL(fileURL).readBytes())
                        Logger.e("下载了视频：$fileURL")
                    } else {
                        Logger.e("从缓存中获取了视频：$fileURL")
                    }
                    return file.path
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ""
            }

            override fun onPostExecute(result: String) {
                postDelayed({
                    if (result.isNotEmpty()) {
                        downLoadListener.onSuccess(result)
                    } else {
                        downLoadListener.onFailure()
                    }
                }, 1000)
            }

        }.execute()
    }

    /**
     * 下载视频监听
     */
    interface DownLoadListener {
        fun onSuccess(filePath: String)
        fun onFailure()
    }
}