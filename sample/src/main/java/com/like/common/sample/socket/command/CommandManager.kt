package com.like.common.sample.socket.command

import com.like.common.sample.socket.client.TCPClient
import com.like.common.sample.socket.client.UDPClient
import com.like.common.sample.socket.message.RealMessageInfo
import com.like.common.sample.socket.message.WrapMessage
import com.like.common.util.RxBusTag
import com.like.logger.Logger
import com.like.rxbus.RxBus
import com.like.rxbus.annotations.RxBusSubscribe

class CommandManager {
    companion object {
        val UDP_CLIENT_PORT = 10000
        val TCP_CLIENT_PORT = 32768 / 2
        // tag
        const val TAG_LIGHT_UP = "TAG_LIGHT_UP"
        const val TAG_LIGHT_OFF = "TAG_LIGHT_OFF"
        const val TAG_INCREASE_BRIGHTNESS = "TAG_INCREASE_BRIGHTNESS"
        const val TAG_REDUCE_BRIGHTNESS = "TAG_REDUCE_BRIGHTNESS"
        const val TAG_SET_LIGHT_COLOR = "TAG_SET_LIGHT_COLOR"
        const val TAG_OPEN_WIFI = "TAG_OPEN_WIFI"
        const val TAG_CLOSE_WIFI = "TAG_CLOSE_WIFI"
        const val TAG_SET_WIFI_NAME = "TAG_SET_WIFI_NAME"
        const val TAG_SET_WIFI_PASSWORD = "TAG_SET_WIFI_PASSWORD"
        const val TAG_SCAN_WIFI = "TAG_SCAN_WIFI"
        const val TAG_INCREASE_VOLUME = "TAG_INCREASE_VOLUME"
        const val TAG_REDUCE_VOLUME = "TAG_REDUCE_VOLUME"
        const val TAG_QUERY_VERSIONINFO = "TAG_QUERY_VERSIONINFO"
        const val TAG_UPDATE_SOUNDBOXSOFT = "TAG_UPDATE_SOUNDBOXSOFT"
        // 灯光相关的命令
        val lightUpCommand = Command(0x1500, 0x0e00, 0x50, byteArrayOf(0x01))// 开灯
        val lightOffCommand = Command(0x1500, 0x0e00, 0x50, byteArrayOf(0x00))// 关灯
        val increaseBrightnessCommand = Command(0x1500, 0x0e00, 0x51, byteArrayOf(0x01))// 增加亮度
        val reduceBrightnessCommand = Command(0x1500, 0x0e00, 0x51, byteArrayOf(0x02))// 减小亮度
        val setLightColorCommand = Command(0x1500, 0x0e00, 0x53)// 设置灯颜色
        // wifi相关的命令
        val openWifiCommand = Command(0x1507, 0x0b00, 0x20, byteArrayOf(0x01))// 打开wifi
        val closeWifiCommand = Command(0x1507, 0x0b00, 0x20, byteArrayOf(0x00))// 关闭wifi
        val setWifiNameCommand = Command(0x1507, 0x0b00, 0x21)// 设置wifi名称
        val setWifiPasswordCommand = Command(0x1507, 0x0b00, 0x22)// 设置wifi密码
        val scanWifiCommand = Command(0x1507, 0x0b00, 0x23)// 扫描wifi
        // 声音相关的命令
        val increaseVolumeCommand = Command(0x1500, 0x1000, 0x10, byteArrayOf(0x01))// 增加音量
        val reduceVolumeCommand = Command(0x1500, 0x1000, 0x10, byteArrayOf(0x02))// 减小音量
        // 公共命令
        val queryVersionInfoCommand = Command(0x1506, 0x0700, 0x03)// 版本信息查询
        val updateSoundBoxSoftCommand = Command(0x1506, 0x0700, 0x05)// 启动软件升级
    }

    private val udpClient: UDPClient = UDPClient(UDP_CLIENT_PORT)
    private val tcpClient: TCPClient = TCPClient(TCP_CLIENT_PORT)

    val commandCache = mutableMapOf<WrapCommand, String>()

    fun connect() {
        tcpClient.setIp("192.168.1.238")// todo 暂时写死，后面需要删除
//        udpClient.start()
        RxBus.register(this)
    }

    /**
     * 版本信息查询
     */
    fun queryVersionInfo() {
        val wrapCommand = WrapCommand(queryVersionInfoCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_QUERY_VERSIONINFO)
        tcpClient.send(wrapCommand)
    }

    /**
     * 启动软件升级
     */
    fun updateSoundBoxSoft() {
        val wrapCommand = WrapCommand(updateSoundBoxSoftCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_UPDATE_SOUNDBOXSOFT)
        tcpClient.send(wrapCommand)
    }

    /**
     * 打开Wifi
     */
    fun openWifi() {
        val wrapCommand = WrapCommand(openWifiCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_OPEN_WIFI)
        tcpClient.send(wrapCommand)
    }

    /**
     * 关闭Wifi
     */
    fun closeWifi() {
        val wrapCommand = WrapCommand(closeWifiCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_CLOSE_WIFI)
        tcpClient.send(wrapCommand)
    }

    /**
     * 设置Wifi名称
     */
    fun setWifiName(name: String) {
        val wrapCommand = WrapCommand(setWifiNameCommand.apply { parameter = name.toByteArray() }).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_SET_WIFI_NAME)
        tcpClient.send(wrapCommand)
    }

    /**
     * 设置Wifi密码
     */
    fun setWifiPassword(password: String) {
        val wrapCommand = WrapCommand(setWifiPasswordCommand.apply { parameter = password.toByteArray() }).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_SET_WIFI_PASSWORD)
        tcpClient.send(wrapCommand)
    }

    /**
     * Wifi扫描
     */
    fun scanWifi() {
        val wrapCommand = WrapCommand(scanWifiCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_SCAN_WIFI)
        tcpClient.send(wrapCommand)
    }

    /**
     * 开灯
     */
    fun lightUp() {
        val wrapCommand = WrapCommand(lightUpCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_LIGHT_UP)
        tcpClient.send(wrapCommand)
    }

    /**
     * 关灯
     */
    fun lightOff() {
        val wrapCommand = WrapCommand(lightOffCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_LIGHT_OFF)
        tcpClient.send(wrapCommand)
    }

    /**
     * 增加灯光亮度
     */
    fun increaseBrightness() {
        val wrapCommand = WrapCommand(increaseBrightnessCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_INCREASE_BRIGHTNESS)
        tcpClient.send(wrapCommand)
    }

    /**
     * 减少灯光亮度
     */
    fun reduceBrightness() {
        val wrapCommand = WrapCommand(reduceBrightnessCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_REDUCE_BRIGHTNESS)
        tcpClient.send(wrapCommand)
    }

    /**
     * 设置灯光颜色
     */
    fun setLightsColor() {
        val wrapCommand = WrapCommand(setLightColorCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_SET_LIGHT_COLOR)
        tcpClient.send(wrapCommand)
    }

    /**
     * 增加音量
     */
    fun increaseVolume() {
        val wrapCommand = WrapCommand(increaseVolumeCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_INCREASE_VOLUME)
        tcpClient.send(wrapCommand)
    }

    /**
     * 减小音量
     */
    fun reduceVolume() {
        val wrapCommand = WrapCommand(reduceVolumeCommand).apply { time = System.currentTimeMillis() }
        commandCache.put(wrapCommand, TAG_REDUCE_VOLUME)
        tcpClient.send(wrapCommand)
    }

    /**
     * 关闭客户端
     */
    fun close() {
//        udpClient.close()
        tcpClient.close()
        RxBus.unregister(this)
    }

//    @RxBusSubscribe(RxBusTag.TAG_UDP_RECEIVE_SUCCESS)
//    fun udpReceivedMessage(udpMessage: UDPClient.UDPMessage) {
//        Logger.i("服务器ip地址：${udpMessage.ip}")
//        tcpClient.setIp(udpMessage.ip)
//    }

    @RxBusSubscribe(RxBusTag.TAG_TCP_RECEIVE_SUCCESS)
    fun tcpReceivedMessage(wrapMessage: WrapMessage) {
        Logger.printMap(commandCache)
        commandCache.filter { it.key.time == wrapMessage.time }.entries.forEach {
            if (wrapMessage.message.isRightMessage(it.key.command)) {
                RxBus.post(it.value, RealMessageInfo().parse(wrapMessage.message.message))
            }
            commandCache.remove(it.key)
            Logger.printMap(commandCache)
        }
    }
}
