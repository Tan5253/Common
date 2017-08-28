package com.like.common.sample.socket

import android.databinding.DataBindingUtil
import android.view.View
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.R
import com.like.common.sample.databinding.ActivitySocketBinding
import com.like.common.sample.socket.command.Message
import com.like.common.sample.socket.command.PublicCommand
import com.like.common.sample.socket.command.WifiCommand
import com.like.common.util.RxBusTag
import com.like.common.util.TCPClient
import com.like.common.util.UDPClient
import com.like.rxbus.annotations.RxBusSubscribe

class SocketActivity : BaseActivity() {
    //    private val udpClient: UDPClient = UDPClient(10000)
    private val tcpClient: TCPClient = TCPClient(32768 / 2)

    private val udpRcvStrBuf = StringBuffer()

    private val mBinding: ActivitySocketBinding by lazy {
        DataBindingUtil.setContentView<ActivitySocketBinding>(this, R.layout.activity_socket)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding
        return null
    }

    fun udpConnect(view: View) {
//        udpClient.start()
//        mBinding.btnCloseUdpAndTcpConnection.isEnabled = true
//        mBinding.btnUdpConnect.isEnabled = false
        tcpClient.setIp("192.168.1.238")
    }

    fun closeUdpAndTcpConnection(view: View) {
//        udpClient.close()
        tcpClient.close()
//        mBinding.btnUdpConnect.isEnabled = true
//        mBinding.btnCloseUdpAndTcpConnection.isEnabled = false
    }

    fun clearReceiveText(view: View) {
        udpRcvStrBuf.delete(0, udpRcvStrBuf.length)
        mBinding.txtRecv.text = udpRcvStrBuf.toString()
    }

    fun queryVersionInfo(view: View) {
        tcpClient.send(PublicCommand().queryVersionInfo())
    }

    fun updateSoundBoxSoft(view: View) {
        tcpClient.send(PublicCommand().updateSoundBoxSoft())
    }

    fun openWifi(view: View) {
        tcpClient.send(WifiCommand().open())
    }

    fun closeWifi(view: View) {
        tcpClient.send(WifiCommand().close())
    }

    fun setWifiName(view: View) {
        tcpClient.send(WifiCommand().setName("wifiName"))
    }

    fun setWifiPassword(view: View) {
        tcpClient.send(WifiCommand().setPassword("wifiPassword"))
    }

    fun scanWifi(view: View) {
        tcpClient.send(WifiCommand().scan())
    }

    @RxBusSubscribe(RxBusTag.TAG_UDP_RECEIVE_SUCCESS)
    fun udpReceivedMessage(udpMessage: UDPClient.UDPMessage) {
        udpRcvStrBuf.append("服务器ip地址：${udpMessage.ip}")
        mBinding.txtRecv.text = udpRcvStrBuf.toString()
        tcpClient.setIp(udpMessage.ip)
    }

    @RxBusSubscribe(RxBusTag.TAG_TCP_RECEIVE_SUCCESS)
    fun tcpReceivedMessage(data: ByteArray) {
        val message = Message().parse(data)
        udpRcvStrBuf.append(message)
        mBinding.txtRecv.text = "$udpRcvStrBuf\n"
    }

    override fun onDestroy() {
        super.onDestroy()
//        udpClient.close()
        tcpClient.close()
    }

}