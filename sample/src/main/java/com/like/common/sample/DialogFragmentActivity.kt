package com.like.common.sample

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityDialogFragmentBinding
import com.like.common.sample.dialog.OneButtonNoTitleDialog
import com.like.common.util.hideDialogFragment
import com.like.common.util.showDialogFragment

/**
 * Created by like on 2017/6/28.
 */
class DialogFragmentActivity : BaseActivity() {

    override fun getViewModel(): BaseViewModel? {
        DataBindingUtil.setContentView<ActivityDialogFragmentBinding>(this, R.layout.activity_dialog_fragment)
        return null
    }

    fun showDialogFragment(view: View) {
        showOneButtonNoTitleDialog()
    }

    fun showOneButtonNoTitleDialog(message: String = "升级成功aaaaaa!", buttonName: String = "完成") {
        val bundle = Bundle()
        bundle.putString(OneButtonNoTitleDialog.KEY_MESSAGE, message)
        bundle.putString(OneButtonNoTitleDialog.KEY_BUTTON_NAME, buttonName)
        Thread {
            showDialogFragment<OneButtonNoTitleDialog>(bundle)
            SystemClock.sleep(5000)
            hideDialogFragment<OneButtonNoTitleDialog>()
        }.start()
    }

}