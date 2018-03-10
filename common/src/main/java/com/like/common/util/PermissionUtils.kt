package com.like.common.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build


object PermissionUtils {

    @JvmStatic
    fun checkPermission(context: Context?, permission: String): Boolean {
        var result = false
        if (context == null || permission.isEmpty()) {
            return result
        }
        if (Build.VERSION.SDK_INT >= 23) {
            result = try {
                val clazz = Class.forName("android.content.Context")
                val method = clazz.getMethod("checkSelfPermission", String::class.java)
                val rest = method.invoke(context, permission) as Int
                rest == PackageManager.PERMISSION_GRANTED
            } catch (e: Throwable) {
                false
            }

        } else {
            val pm = context.packageManager
            if (pm.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED) {
                result = true
            }
        }
        return result
    }
}
