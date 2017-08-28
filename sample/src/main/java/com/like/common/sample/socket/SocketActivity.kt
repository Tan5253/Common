package com.like.common.sample.socket

import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.R
import com.like.common.sample.databinding.ActivitySocketBinding
import com.like.common.sample.socket.command.CommandManager
import com.like.common.sample.socket.command.Message
import com.like.common.util.RxBusTag
import com.like.rxbus.annotations.RxBusSubscribe

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
        commandManager.openLights()
    }

    fun closeLights(view: View) {
        commandManager.closeLights()
    }

    fun increaseLightsBrightness(view: View) {
        commandManager.increaseLightsBrightness()
    }

    fun reduceLightsBrightness(view: View) {
        commandManager.reduceLightsBrightness()
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

    @RxBusSubscribe(RxBusTag.TAG_TCP_RECEIVE_SUCCESS)
    fun tcpReceivedMessage(data: ByteArray) {
        val message = Message().parse(data)
        udpRcvStrBuf.append(message)
        mBinding.txtRecv.text = "$udpRcvStrBuf\n"
    }

    override fun onDestroy() {
        super.onDestroy()
        commandManager.close()
    }

}