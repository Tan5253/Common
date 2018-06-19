package com.like.common.base.context;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.like.toast.ToastUtilsKt;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 建议启动页面继承此Activity来获取应用需要的危险权限
 * <p>
 * 运行时动态请求危险权限。包括：<br/>
 * CALENDAR     |   READ_CALENDAR、WRITE_CALENDAR<br/>
 * CAMERA       |   CAMERA<br/>
 * CONTACTS     |   READ_CONTACTS、WRITE_CONTACTS、GET_ACCOUNTS<br/>
 * LOCATION     |   ACCESS_FINE_LOCATION、ACCESS_COARSE_LOCATION<br/>
 * MICROPHONE   |   RECORD_AUDIO<br/>
 * PHONE        |   READ_PHONE_STATE、CALL_PHONE、READ_CALL_LOG、WRITE_CALL_LOG、ADD_VOICEMAIL、USE_SIP、PROCESS_OUTGOING_CALLS<br/>
 * SENSORS      |   BODY_SENSORS<br/>
 * SMS          |   SEND_SMS、RECEIVE_SMS、READ_SMS、RECEIVE_WAP_PUSH、RECEIVE_MMS<br/>
 * STORAGE      |   READ_EXTERNAL_STORAGE、WRITE_EXTERNAL_STORAGE<br/>
 * <p>
 * 注意：<br/>
 * ①同一组权限中，申请了其中一个，则此组中所有权限都申请了。<br/>
 * ②申请的权限必须在AndroidManifest.xml中申明，否则不会弹出系统权限授权的对话框。<br/>
 */
public abstract class BasePermissionActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    // 权限请求码
    private static final int REQUEST_CODE_PERMISSION = 10000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    // 同意了所有请求的权限
    @Override
    public void onPermissionsGranted(int requestCode, List<String> permissions) {
    }

    // 一个或者多个请求权限被拒绝
    @Override
    public void onPermissionsDenied(int requestCode, List<String> permissions) {
        // 有权限被永久的拒绝
        if (EasyPermissions.somePermissionPermanentlyDenied(this, permissions)) {
            new AppSettingsDialog
                    .Builder(this)
                    .setTitle("权限申请失败")
                    .setRationale("我们需要的一些权限被您拒绝，导致功能无法正常使用。请您到设置页面手动授权！")
                    .build()
                    .show();
        } else {
            ToastUtilsKt.shortToastCenter(this, "我们需要的一些权限被您拒绝，导致要应用无法正常使用！");
        }
        finish();
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION)
    private void requestPermissions() {
        // 没有权限时，请求相关权限
        if (!EasyPermissions.hasPermissions(this, getPermissions())) {
            EasyPermissions.requestPermissions(this, getRationale(), REQUEST_CODE_PERMISSION, getPermissions());
        } else {
            hasPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从设置界面返回
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
        }
    }

    /**
     * 申请的所有权限
     *
     * @return
     */
    protected abstract String[] getPermissions();

    /**
     * 关于所申请的权限的解释
     *
     * @return
     */
    protected abstract String getRationale();

    /**
     * 已经拥有了相关权限
     */
    protected abstract void hasPermissions();

}