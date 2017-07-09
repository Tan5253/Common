package com.like.common.util

import android.content.Context
import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException


class UDPClient(val context: Context) : Runnable {
    companion object {
        private val BUFFER_SIZE = 1024
        private val RECEIVE_TIME_OUT = 5000
        private val socket: DatagramSocket = DatagramSocket()
    }

    val udpPort = 6000
    val hostIp = "192.168.1.102"
    var udpLife = true //udp生命线程

    // 发送消息
    fun send(message: String) {
        try {
            val sendBytes = message.toByteArray(charset("UTF-8"))
            socket.send(DatagramPacket(sendBytes, sendBytes.size, InetAddress.getByName(hostIp), udpPort))
        } catch (e: Exception) {
            Logger.e("发送数据失败！")
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            socket.soTimeout = RECEIVE_TIME_OUT
        } catch (e: Exception) {
            Logger.e("初始化接收数据端失败！")
            e.printStackTrace()
            Logger.i("UDP监听关闭")
            socket.close()
            return
        }
        while (udpLife) {
            try {
                Logger.i("UDP监听中……")
                val buf = ByteArray(BUFFER_SIZE)
                val packetRcv = DatagramPacket(buf, buf.size)
                socket.receive(packetRcv)
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
        Logger.i("UDP监听关闭")
        socket.close()
    }

}