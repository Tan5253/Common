package com.like.common.util

import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.Executors
import kotlin.concurrent.thread


/**
 * socket TCP 接收、发送数据的客户端
 *
 * @param port              服务器端口号
 * @param readBufferSize    接收器的缓存大小，默认1024kb
 * @param readTimeOut       接收器每次读取数据的超时时长，默认10000毫秒
 */
class TCPClient(val port: Int, val readBufferSize: Int = 1024, val readTimeOut: Int = 10000) : Runnable {
    private val executors = Executors.newCachedThreadPool()
    private var life = false
    private var ip: String = ""

    fun start(ip: String) {
        this.ip = ip
        if (!life) {
            life = true
            executors.execute(this)
        }
    }

    fun close() {
        life = false
    }

    fun send(message: String) {
        thread {
            try {
                val socket = Socket(ip, port)
                val dos = DataOutputStream(socket.getOutputStream())
                dos.writeUTF(message)
                dos.flush()
                dos.close()
                socket.close()
                Logger.i("TCP发送数据成功")
            } catch (e: Exception) {
                Logger.e("TCP发送数据失败！")
                e.printStackTrace()
            }
        }
    }

    override fun run() {
        val socket: Socket
        val dis: DataInputStream
        try {
            socket = Socket(ip, port)
            socket.soTimeout = readTimeOut
            dis = DataInputStream(socket.getInputStream())
            Logger.i("初始化TCP客户端成功")
        } catch (e: Exception) {
            Logger.e("初始化TCP客户端失败！")
            e.printStackTrace()
            return
        }

        val buf = ByteArray(readBufferSize)
        while (life) {
            Logger.i("TCP监听中……")
            try {
                val length = dis.read(buf)
                val msgRcv = String(buf, 0, length, Charsets.UTF_8)
                Logger.i("TCP接收到消息：$msgRcv")
                RxBus.post(RxBusTag.TAG_TCP_RECEIVE_SUCCESS, msgRcv)
            } catch (e: Exception) {
                Logger.e("TCP接收消息失败！")
                e.printStackTrace()
            }
        }
        dis.close()
        socket.close()
        Logger.i("TCP监听关闭")
    }

}