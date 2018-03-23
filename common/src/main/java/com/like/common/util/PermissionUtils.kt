package com.like.common.util

import android.app.Activity
import android.support.v4.app.ActivityCompat
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

object PermissionUtils {

    fun checkPermissionsAndRun(activity: Activity, rationale: String, requestCode: Int, block: () -> Unit, vararg permissions: String) {
        if (!EasyPermissions.hasPermissions(activity, *permissions)) {
            if (shouldShowRequestPermissionRationale(activity, *permissions)) {
                AppSettingsDialog.Builder(activity)
                        .setTitle("权限申请失败")
                        .setRationale("我们需要的一些权限被您拒绝，导致功能无法正常使用。请您到设置页面手动授权！")
                        .build()
                        .show()
            } else {
                EasyPermissions.requestPermissions(activity, rationale, requestCode, *permissions)
            }
        } else {
            block.invoke()
        }
    }

    private fun shouldShowRequestPermissionRationale(activity: Activity, vararg permissions: String): Boolean {
        permissions.forEach {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {// 选择了拒绝并且不再提醒
                return true
            }
        }
        return false
    }
}
