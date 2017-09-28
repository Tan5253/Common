package com.like.common.view.dragvideoview

import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.R

class DragVideoViewActivity : BaseActivity() {
    companion object {
        const val KEY_DATA = "dragVideoViewInfo"
    }

    private lateinit var dragVideoViewInfo: DragVideoViewInfo

    private val dragVideoView: DragVideoView by lazy {
        DragVideoView(this, dragVideoViewInfo)
    }

    override fun getViewModel(): BaseViewModel? {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getColor(int id)在API版本23时(Android 6.0)已然过时
            //从这里我们可以看出，当API版本>=23时，使用ContextCompatApi23.getColor(context, id)方法，
            //当API版本<23时，使用context.getResources().getColor(id)方法获取相应色值
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            window.statusBarColor = ContextCompat.getColor(this, R.color.common_transparent)
        }

        dragVideoViewInfo = intent.getParcelableExtra(KEY_DATA)

        setContentView(dragVideoView)

        return null
    }

    override fun onBackPressed() {
        dragVideoView.disappear()
    }
}