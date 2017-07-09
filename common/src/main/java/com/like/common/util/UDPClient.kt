package com.like.common.util

import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.util.concurrent.Executors

/**
 * socket UDP 接收、发送数据的客户端
 *
 * @param port                  服务器端口号
 * @param receiverBufferSize    接收器的缓存大小，默认1024kb
 * @param receiverTimeOut       接收器每次读取数据的超时时长，默认5000毫秒
 */
class UDPClient(val port: Int, val receiverBufferSize: Int = 1024, val receiverTimeOut: Int = 5000) : Runnable {
    private val executors = Executors.newCachedThreadPool()
    private var udpLife = false // udp生命线程
    private var socket: DatagramSocket? = null
    private val ip = "192.168.1.102"

    fun start() {
        if (!udpLife && socket == null) {
            udpLife = true
            socket = DatagramSocket()
            executors.execute(this)
            sendBroadcast()
        }
    }

    fun close() {
        udpLife = false
        Logger.i("UDP监听关闭")
        socket?.close()
        socket = null
    }

    fun send(message: String) {
        try {
            val sendBytes = message.toByteArray(charset("UTF-8"))
            socket?.send(DatagramPacket(sendBytes, sendBytes.size, InetAddress.getByName(ip), port))
        } catch (e: Exception) {
            Logger.e("发送数据失败！")
            e.printStackTrace()
        }
    }

    /**
     * 发送广播，用于通知硬件客户端的ip和port。
     */
    private fun sendBroadcast() {
        try {
            socket?.send(DatagramPacket(byteArrayOf(), 0, InetAddress.getByName("255.255.255.255"), port))
        } catch (e: Exception) {
            Logger.e("发送广播数据失败！")
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            socket?.soTimeout = receiverTimeOut
        } catch (e: Exception) {
            Logger.e("初始化接收数据端失败！")
            e.printStackTrace()
            close()
            return
        }
        while (udpLife) {
            try {
                Logger.i("UDP监听中……")
                val buf = ByteArray(receiverBufferSize)
                val packetRcv = DatagramPacket(buf, buf.size)
                socket?.receive(packetRcv)
                val RcvMsg = String(packetRcv.data, packetRcv.offset, packetRcv.length, charset("UTF-8"))
                RxBus.post(RxBusTag.TAG_UDP_RECEIVE_SUCCESS, RcvMsg)
                Logger.i("接收到消息：$RcvMsg")
            } catch (e1: SocketTimeoutException) {
                Logger.w("没有收到数据")
                e1.printStackTrace()
            } catch (e: Exception) {
                Logger.e("接收数据失败！")
                e.printStackTrace()
            }
        }
        close()
    }

}