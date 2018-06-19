package com.like.common.base.entity

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.FragmentManager
import com.like.common.base.context.BaseActivity
import com.like.common.base.context.BaseFragment
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import org.jetbrains.anko.startActivity
import java.io.Serializable
import java.util.*

/**
 * 宿主信息。包含host(宿主)、isForeground(宿主是否前台标记)。
 *
 * @param host 宿主引用。可以是BaseActivity或者BaseFragment。
 *
 * @author like
 * @version 1.0
 * created on 2017/4/21 9:00
 */
class Host(val host: Any) : Serializable {
    /**
     * 宿主当前是否在前台。
     * 此标记在BaseActivity和BaseFragment处于前台显示时被设置为true，后台显示时被设置为false。
     * 可以根据此标记来控制是否接收RxBus消息。
     */
    var isForeground: Boolean = false

    val activity: Activity by lazy { host as? BaseActivity ?: (host as BaseFragment).activity }

    val applicationContext: Context by lazy { activity.applicationContext }

    val intent: Intent by lazy { activity.intent }

    val fragmentManager: FragmentManager by lazy {
        when (host) {
            is BaseActivity -> host.supportFragmentManager
            else -> (host as BaseFragment).fragmentManager
        }
    }

    init {
        if (host !is BaseActivity && host !is BaseFragment) {
            throw IllegalArgumentException("host 只能是BaseActivity或者BaseFragment类型")
        }
    }

    inline fun <reified T : Activity> startActivity(vararg params: Pair<String, Any>) {
        activity.startActivity<T>(*params)
    }

    fun startActivity(intent: Intent) {
        activity.startActivity(intent)
    }

    /**
     * 如果是Activity，直接关闭；如果是Fragment，则关闭它对应的Activity。
     */
    fun finish() {
        activity.finish()
    }

    /**
     * 绑定宿主的onDestroy()方法
     *
     * @param <T>
     * @return
    </T> */
    fun <T> bindDestroyEvent(): LifecycleTransformer<T>? {
        if (host is RxAppCompatActivity) {
            return (host as RxAppCompatActivity).bindUntilEvent(ActivityEvent.DESTROY)
        } else if (host is RxFragment) {
            return (host as RxFragment).bindUntilEvent(FragmentEvent.DESTROY)
        }
        return null
    }

    fun getBooleanExtra(name: String, defaultValue: Boolean): Boolean = intent.getBooleanExtra(name, defaultValue)

    fun getByteExtra(name: String, defaultValue: Byte): Byte = intent.getByteExtra(name, defaultValue)

    fun getShortExtra(name: String, defaultValue: Short): Short = intent.getShortExtra(name, defaultValue)

    fun getCharExtra(name: String, defaultValue: Char): Char = intent.getCharExtra(name, defaultValue)

    fun getIntExtra(name: String, defaultValue: Int): Int = intent.getIntExtra(name, defaultValue)

    fun getLongExtra(name: String, defaultValue: Long): Long = intent.getLongExtra(name, defaultValue)

    fun getFloatExtra(name: String, defaultValue: Float): Float = intent.getFloatExtra(name, defaultValue)

    fun getDoubleExtra(name: String, defaultValue: Double): Double = intent.getDoubleExtra(name, defaultValue)

    fun getStringExtra(name: String): String = intent.getStringExtra(name)

    fun getCharSequenceExtra(name: String): CharSequence = intent.getCharSequenceExtra(name)

    fun <T : Parcelable> getParcelableExtra(name: String): T = intent.getParcelableExtra(name)

    fun getParcelableArrayExtra(name: String): Array<Parcelable> = intent.getParcelableArrayExtra(name)

    fun <T : Parcelable> getParcelableArrayListExtra(name: String): ArrayList<T> = intent.getParcelableArrayListExtra(name)

    fun getSerializableExtra(name: String): Serializable = intent.getSerializableExtra(name)

    fun getIntegerArrayListExtra(name: String): ArrayList<Int> = intent.getIntegerArrayListExtra(name)

    fun getStringArrayListExtra(name: String): ArrayList<String> = intent.getStringArrayListExtra(name)

    fun getCharSequenceArrayListExtra(name: String): ArrayList<CharSequence> = intent.getCharSequenceArrayListExtra(name)

    fun getBooleanArrayExtra(name: String): BooleanArray = intent.getBooleanArrayExtra(name)

    fun getByteArrayExtra(name: String): ByteArray = intent.getByteArrayExtra(name)

    fun getShortArrayExtra(name: String): ShortArray = intent.getShortArrayExtra(name)

    fun getCharArrayExtra(name: String): CharArray = intent.getCharArrayExtra(name)

    fun getIntArrayExtra(name: String): IntArray = intent.getIntArrayExtra(name)

    fun getLongArrayExtra(name: String): LongArray = intent.getLongArrayExtra(name)

    fun getFloatArrayExtra(name: String): FloatArray = intent.getFloatArrayExtra(name)

    fun getDoubleArrayExtra(name: String): DoubleArray = intent.getDoubleArrayExtra(name)

    fun getStringArrayExtra(name: String): Array<String> = intent.getStringArrayExtra(name)

    fun getCharSequenceArrayExtra(name: String): Array<CharSequence> = intent.getCharSequenceArrayExtra(name)

    fun getBundleExtra(name: String): Bundle = intent.getBundleExtra(name)

    fun getExtras(): Bundle = intent.getExtras()

    fun getFlags(): Int = intent.getFlags()

    fun getPackage(): String = intent.getPackage()

    fun getComponent(): ComponentName = intent.getComponent()

    fun getSourceBounds(): Rect = intent.getSourceBounds()

}
