package com.like.common.util

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SPUtils private constructor() {
    private val suffix = ".sharedpreferences"
    private val prefs: SharedPreferences by lazy {
        mContext.getSharedPreferences("${mContext.packageName}$suffix", Context.MODE_PRIVATE)
    }

    companion object {
        private lateinit var mContext: Context

        @JvmStatic fun getInstance(context: Context): SPUtils {
            mContext = context
            return Holder.instance
        }
    }

    private object Holder {
        val instance = SPUtils()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, default: T): T = with(prefs) {
        val result: Any = when (default) {
            is String -> getString(key, default)
            is Boolean -> getBoolean(key, default)
            is Int -> getInt(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
        }
        result as T
    }

    fun <T> put(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
        }.apply()
    }

    /**
     * 移除某个key对应的那一条数据
     * @param key
     */
    fun remove(key: String) = prefs.edit().remove(key).apply()

    /**
     * 清除所有数据
     */
    fun clear() = prefs.edit().clear().apply()

    /**
     * 查询某个key是否已经存在

     * @param key
     * @return
     */
    fun contains(key: String): Boolean = prefs.contains(key)

    /**
     * 返回所有的键值对数据
     * @return
     */
    fun getAll(): Map<String, *> = prefs.all
}

/**
 * SharedPreferences属性委托
 *
 * 示例：var k: Long by DelegateSharedPreferences(this, "long", 10L)
 *
 * @property context
 * @property key        存储的key
 * @property default    获取失败时，返回的默认值
 */
class DelegateSharedPreferences<T>(val context: Context, val key: String, val default: T) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return SPUtils.getInstance(context).get(key, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        SPUtils.getInstance(context).put(key, value)
    }

}
