package com.like.common.sample.socket.command

import com.like.common.util.TCPClient
import com.like.common.util.UDPClient

class CommandManager {
    companion object {
        val UDP_CLIENT_PORT = 10000
        val TCP_CLIENT_PORT = 32768 / 2
    }

    private val udpClient: UDPClient = UDPClient(UDP_CLIENT_PORT)
    private val tcpClient: TCPClient = TCPClient(TCP_CLIENT_PORT)

    fun connect() {
        tcpClient.setIp("192.168.1.238")// todo 暂时写死，后面需要删除
        udpClient.start()
    }

    /**
     * 版本信息查询
     */
    fun queryVersionInfo() {
        tcpClient.send(PublicCommand().queryVersionInfo())
    }

    /**
     * 启动软件升级
     */
    fun updateSoundBoxSoft() {
        tcpClient.send(PublicCommand().updateSoundBoxSoft())
    }

    /**
     * 打开Wifi
     */
    fun openWifi() {
        tcpClient.send(WifiCommand().open())
    }

    /**
     * 关闭Wifi
     */
    fun closeWifi() {
        tcpClient.send(WifiCommand().close())
    }

    /**
     * 设置Wifi名称
     */
    fun setWifiName(name: String) {
        tcpClient.send(WifiCommand().setName(name))
    }

    /**
     * 设置Wifi密码
     */
    fun setWifiPassword(password: String) {
        tcpClient.send(WifiCommand().setPassword("wifiPassword"))
    }

    /**
     * Wifi扫描
     */
    fun scanWifi() {
        tcpClient.send(WifiCommand().scan())
    }

    /**
     * 开灯
     */
    fun openLights() {
        tcpClient.send(LightsCommand().open())
    }

    /**
     * 关灯
     */
    fun closeLights() {
        tcpClient.send(LightsCommand().close())
    }

    /**
     * 增加灯光亮度
     */
    fun increaseLightsBrightness() {
        tcpClient.send(LightsCommand().increaseBrightness())
    }

    /**
     * 减少灯光亮度
     */
    fun reduceLightsBrightness() {
        tcpClient.send(LightsCommand().reduceBrightness())
    }

    /**
     * 设置灯光颜色
     */
    fun setLightsColor() {
        tcpClient.send(LightsCommand().setColor())
    }

    /**
     * 增加音量
     */
    fun increaseVolume() {
        tcpClient.send(VolumeCommand().increaseVolume())
    }

    /**
     * 减小音量
     */
    fun reduceVolume() {
        tcpClient.send(VolumeCommand().reduceVolume())
    }

    /**
     * 关闭客户端
     */
    fun close() {
        udpClient.close()
        tcpClient.close()
    }

//    @RxBusSubscribe(RxBusTag.TAG_UDP_RECEIVE_SUCCESS)
//    fun udpReceivedMessage(udpMessage: UDPClient.UDPMessage) {
//        Logger.i("服务器ip地址：${udpMessage.ip}")
//        tcpClient.setIp(udpMessage.ip)
//    }
}
