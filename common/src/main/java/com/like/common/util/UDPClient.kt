package com.like.common.util

import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * socket UDP 接收、发送数据的客户端
 *
 * @param port                  服务器端口号
 * @param receiverBufferSize    接收器的缓存大小，默认1024kb
 * @param receiverTimeOut       接收器每次读取数据的超时时长，默认Int.MAX_VALUE毫秒
 */
class UDPClient(val port: Int, val receiverBufferSize: Int = 1024, val receiverTimeOut: Int = Int.MAX_VALUE) : Runnable {
    private var executors: ExecutorService? = null
    private val broadcastIpAddress = InetAddress.getByName("255.255.255.255")// 广播地址，用于通知硬件客户端的ip和port。
    private var ipAddress: InetAddress? = null
    private var life = false
    private var socket: DatagramSocket? = null

    fun start() {
        if (!life) {
            life = true
            executors = Executors.newCachedThreadPool()
            executors?.execute(this)
        }
    }

    fun close() {
        life = false
        socket?.close()
        socket = null
        executors?.shutdownNow()
        executors = null
        Logger.i("UDP监听关闭")
    }

    fun setIp(ip: String) {
        try {
            ipAddress = InetAddress.getByName(ip)
        } catch (e: Exception) {
            Logger.e("UDP设置ip地址失败！")
            e.printStackTrace()
        }
    }

    fun send(message: String) {
        thread {
            try {
                val sendBytes = message.toByteArray(Charsets.UTF_8)
                socket!!.send(DatagramPacket(sendBytes, sendBytes.size, ipAddress, port))
                Logger.i("UDP发送数据成功")
            } catch (e: Exception) {
                Logger.e("UDP发送数据失败！")
                e.printStackTrace()
            }
        }
    }

    override fun run() {
        try {
            socket = DatagramSocket()
            socket!!.soTimeout = receiverTimeOut
            // 发送广播，用于通知硬件客户端的ip和port。
            socket!!.send(DatagramPacket(byteArrayOf(), 0, broadcastIpAddress, port))
            Logger.i("初始化UDP客户端成功")
        } catch (e: Exception) {
            Logger.e("初始化UDP客户端失败！")
            e.printStackTrace()
            close()
            return
        }
        while (life) {
            try {
                Logger.i("UDP监听中……")
                val buf = ByteArray(receiverBufferSize)
                val packetRcv = DatagramPacket(buf, buf.size)
                socket!!.receive(packetRcv)
                ipAddress = packetRcv.address
                val RcvMsg = String(packetRcv.data, packetRcv.offset, packetRcv.length, Charsets.UTF_8)
                RxBus.post(RxBusTag.TAG_UDP_RECEIVE_SUCCESS, UDPMessage(ipAddress?.hostAddress!!, RcvMsg))
                Logger.i("UDP接收到消息：$RcvMsg")
            } catch (e1: SocketTimeoutException) {
                Logger.w("UDP没有收到数据")
                e1.printStackTrace()
            } catch (e: Exception) {
                Logger.e("UDP接收数据失败！")
                e.printStackTrace()
            }
        }
        close()
    }

    data class UDPMessage(val ip: String, val message: String)

}