package com.like.common.util

import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * socket TCP 接收、发送数据的客户端
 *
 * @param port              服务器端口号
 * @param readBufferSize    接收器的缓存大小，默认1024kb
 * @param readTimeOut       接收器每次读取数据的超时时长，默认10000毫秒
 */
class TCPClient(val port: Int, val readBufferSize: Int = 1024, val readTimeOut: Int = 3000) : Runnable {
    private val executors = Executors.newCachedThreadPool()
    private var life: AtomicBoolean = AtomicBoolean(false)
    private var ip: String = ""
    private var socket: Socket? = null
    private var dos: DataOutputStream? = null
    private var dis: DataInputStream? = null

    fun start(ip: String) {
        this.ip = ip
        if (!life.get()) {
            life.set(true)
            executors.execute(this)
        }
    }

    fun close() {
        life.set(false)
        try {
            dis?.close()
            dos?.close()
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Logger.i("TCP监听关闭")
    }

    fun restart() {
        Logger.i("TCP restart")
        close()
        start(ip)
    }

    fun send(message: String) {
        thread {
            try {
                dos!!.writeUTF(message)
                dos!!.flush()
                Logger.i("TCP发送数据成功")
            } catch (e: Exception) {
                Logger.e("TCP发送数据失败！")
                e.printStackTrace()
            }
        }
    }

    override fun run() {
        try {
            socket = Socket(ip, port)
            socket!!.soTimeout = readTimeOut
            dos = DataOutputStream(socket!!.getOutputStream())
            dis = DataInputStream(socket!!.getInputStream())
        } catch (e: Exception) {
            Logger.e("初始化TCP客户端失败！")
            e.printStackTrace()
            return
        }
        val buf = ByteArray(readBufferSize)
        while (life.get()) {
            try {
                Logger.i("TCP监听中……")
                val length = dis!!.read(buf)
                if (length > 0) {
                    val msgRcv = String(buf, 0, length, Charsets.UTF_8)
                    RxBus.post(RxBusTag.TAG_TCP_RECEIVE_SUCCESS, msgRcv)
                    Logger.i("TCP接收到消息：$msgRcv")
                } else {
                    Logger.e("TCP接收消息失败！ length==0")
                }
            } catch (e1: SocketTimeoutException) {
                Logger.w("TCP没有收到数据 SocketTimeoutException")
                e1.printStackTrace()
            } catch (e: Exception) {
                Logger.e("TCP接收消息失败！ Exception")
                e.printStackTrace()
            }
        }
    }

}