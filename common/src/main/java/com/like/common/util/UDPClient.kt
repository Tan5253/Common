package com.like.common.util

import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.util.concurrent.Executors

class UDPClient : Runnable {
    companion object {
        private val DEFAULT_BUFFER_SIZE = 1024
        private val DEFAULT_RECEIVE_TIME_OUT = 5000
        private val PORT = 6000
    }

    val ip = "192.168.1.102"

    private val executors = Executors.newCachedThreadPool()
    private var udpLife = true //udp生命线程
    private var socket: DatagramSocket? = null

    fun start() {
        udpLife = true
        socket = DatagramSocket()
        executors.execute(this)
    }

    fun close() {
        udpLife = false
        Logger.i("UDP监听关闭")
        socket?.close()
        socket = null
    }

    fun sendBroadcast() {
        try {
            socket?.send(DatagramPacket(byteArrayOf(), 0, InetAddress.getByName("255.255.255.255"), PORT))
        } catch (e: Exception) {
            Logger.e("发送广播数据失败！")
            e.printStackTrace()
        }
    }

    fun send(message: String) {
        try {
            val sendBytes = message.toByteArray(charset("UTF-8"))
            socket?.send(DatagramPacket(sendBytes, sendBytes.size, InetAddress.getByName(ip), PORT))
        } catch (e: Exception) {
            Logger.e("发送数据失败！")
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            socket?.soTimeout = DEFAULT_RECEIVE_TIME_OUT
        } catch (e: Exception) {
            Logger.e("初始化接收数据端失败！")
            e.printStackTrace()
            close()
            return
        }
        while (udpLife) {
            try {
                Logger.i("UDP监听中……")
                val buf = ByteArray(DEFAULT_BUFFER_SIZE)
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