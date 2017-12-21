package com.like.common.util

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.like.base.context.BaseActivity
import com.like.base.context.BaseFragment
import com.like.base.entity.Host

/**
 * 显示DialogFragment对话框
 *
 * @param T         目标DialogFragment
 * @param args      参数列表
 */
inline fun <reified T : DialogFragment> BaseActivity.showDialogFragment(args: Bundle? = null): DialogFragment? = DialogFragmentUtils.showDialog(this, T::class.java, args)

/**
 * 显示DialogFragment对话框
 *
 * @param T         目标DialogFragment
 * @param args      参数列表
 */
inline fun <reified T : DialogFragment> BaseFragment.showDialogFragment(args: Bundle? = null): DialogFragment? = DialogFragmentUtils.showDialog(this, T::class.java, args)

/**
 * 隐藏DialogFragment对话框
 *
 * @param T         目标DialogFragment
 */
inline fun <reified T : DialogFragment> BaseActivity.hideDialogFragment() = DialogFragmentUtils.hideDialog(this, T::class.java)

/**
 * 隐藏DialogFragment对话框
 *
 * @param T         目标DialogFragment
 */
inline fun <reified T : DialogFragment> BaseFragment.hideDialogFragment() = DialogFragmentUtils.hideDialog(this, T::class.java)

object DialogFragmentUtils {
    /**
     * 显示指定的DialogFragment对话框
     *
     * @param host                  [BaseActivity]或者[BaseFragment]或者[Host]
     * @param dialogFragmentClass   目标DialogFragment
     * @param args                  参数列表
     */
    @JvmStatic
    @JvmOverloads
    fun showDialog(host: Any, dialogFragmentClass: Class<out DialogFragment>, args: Bundle? = null): DialogFragment? {
        val fm = getFragmentManager(host) ?: return null
        val dialogFragment = dialogFragmentClass.newInstance() ?: return null
        // 放入参数
        if (args != null) {
            dialogFragment.arguments = args
        }

        // 先移除，再添加并显示
        val ft = fm.beginTransaction()
        if (ft != null) {
            val preFragment = fm.findFragmentByTag(dialogFragmentClass.simpleName)
            if (preFragment != null) {
                ft.remove(preFragment)
            }
            ft.add(dialogFragment, dialogFragmentClass.simpleName)
            ft.commitAllowingStateLoss()
        }
        return dialogFragment
    }

    /**
     * 隐藏指定的DialogFragment对话框
     *
     * @param host                  [BaseActivity]或者[BaseFragment]或者[Host]
     * @param dialogFragmentClass   目标DialogFragment
     */
    @JvmStatic
    fun hideDialog(host: Any, dialogFragmentClass: Class<out DialogFragment>) {
        val fm = getFragmentManager(host) ?: return
        val dialog = fm.findFragmentByTag(dialogFragmentClass.simpleName)
        if (dialog != null && !dialog.isHidden && dialog is DialogFragment) {
            dialog.dismissAllowingStateLoss()
        }
    }

    private fun getFragmentManager(host: Any): FragmentManager? = if (host is Host) {
        host.fragmentManager
    } else if (host is BaseActivity) {
        host.supportFragmentManager
    } else if (host is BaseFragment) {
        host.fragmentManager
    } else {
        null
    }
}

abstract class BaseDialogFragment<in T : ViewDataBinding> : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar)
        isCancelable = cancelable()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<T>(inflater, getDialogFragmentLayoutResId(), container, false) ?: return null
        initView(binding, arguments)
        if (!cancelableOnKeyBackPressed()) {
            dialog.setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
        return binding.root
    }

    fun hide() {
        this.dismissAllowingStateLoss()
    }

    /**
     * 按返回键时退出对话框
     */
    open fun cancelableOnKeyBackPressed() = true

    open fun cancelable() = true

    abstract fun getDialogFragmentLayoutResId(): Int

    abstract fun initView(binding: T, arguments: Bundle?)
}