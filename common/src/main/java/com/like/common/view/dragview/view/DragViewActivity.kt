package com.like.common.view.dragview.view

import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.R
import com.like.common.view.dragphotoview.DragPhotoViewActivity
import com.like.common.view.dragview.entity.DragInfo
import java.lang.Exception

class DragViewActivity : BaseActivity() {
    companion object {
        const val KEY_DATA = "DragInfo"
    }

    private lateinit var view: BaseDragView

    override fun getViewModel(): BaseViewModel? {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getColor(int id)在API版本23时(Android 6.0)已然过时
            //从这里我们可以看出，当API版本>=23时，使用ContextCompatApi23.getColor(context, id)方法，
            //当API版本<23时，使用context.getResources().getColor(id)方法获取相应色值
            //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            window.statusBarColor = ContextCompat.getColor(this, R.color.common_transparent)
        }

        try {
            val infos: List<DragInfo> = intent.getParcelableArrayListExtra(DragPhotoViewActivity.KEY_DATA)
            view = DragPhotoView(this, infos)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val info: DragInfo = intent.getParcelableExtra(KEY_DATA)
            view = DragVideoView(this, info)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setContentView(view)

        return null
    }

    override fun onBackPressed() {
        view.disappear()
    }
}