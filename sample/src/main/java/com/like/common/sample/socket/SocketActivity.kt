package com.like.common.sample.socket

import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.R
import com.like.common.sample.databinding.ActivitySocketBinding
import com.like.common.sample.socket.command.CommandManager

class SocketActivity : BaseActivity() {
    private val commandManager: CommandManager by lazy { CommandManager() }

    private val udpRcvStrBuf = StringBuffer()

    private val mBinding: ActivitySocketBinding by lazy {
        DataBindingUtil.setContentView<ActivitySocketBinding>(this, R.layout.activity_socket)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding
        return null
    }

    fun udpConnect(view: View) {
        commandManager.connect()
    }

    fun closeUdpAndTcpConnection(view: View) {
        commandManager.close()
    }

    fun clearReceiveText(view: View) {
        udpRcvStrBuf.delete(0, udpRcvStrBuf.length)
        mBinding.txtRecv.text = udpRcvStrBuf.toString()
    }

    fun queryVersionInfo(view: View) {
        commandManager.queryVersionInfo()
    }

    fun updateSoundBoxSoft(view: View) {
        commandManager.updateSoundBoxSoft()
    }

    fun openWifi(view: View) {
        commandManager.openWifi()
    }

    fun closeWifi(view: View) {
        commandManager.closeWifi()
    }

    fun setWifiName(view: View) {
        commandManager.setWifiName("wifiName")
    }

    fun setWifiPassword(view: View) {
        commandManager.setWifiPassword("wifiPassword")
    }

    fun scanWifi(view: View) {
        commandManager.scanWifi()
    }

    fun openLights(view: View) {
        commandManager.lightUp()
    }

    fun closeLights(view: View) {
        commandManager.lightOff()
    }

    fun increaseLightsBrightness(view: View) {
        commandManager.increaseBrightness()
    }

    fun reduceLightsBrightness(view: View) {
        commandManager.reduceBrightness()
    }

    fun setLightsColor(view: View) {
        commandManager.setLightsColor()
    }

    fun increaseVolume(view: View) {
        commandManager.increaseVolume()
    }

    fun reduceVolume(view: View) {
        commandManager.reduceVolume()
    }

    override fun onDestroy() {
        super.onDestroy()
        commandManager.close()
    }

}