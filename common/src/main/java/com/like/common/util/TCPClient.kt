package com.like.common.util

import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.concurrent.Executors


/**
 * socket TCP 接收、发送数据的客户端
 *
 * @param port                  服务器端口号
 * @param receiverBufferSize    接收器的缓存大小，默认1024kb
 * @param receiverTimeOut       接收器每次读取数据的超时时长，默认10000毫秒
 */
class TCPClient(val port: Int, val receiverBufferSize: Int = 1024, val receiverTimeOut: Int = 10000) : Runnable {
    private val executors = Executors.newCachedThreadPool()
    private var lift = false // udp生命线程
    private var socket: Socket? = null

    fun start(ip: String) {
        if (!lift && socket == null) {
            lift = true
            try {
                socket = Socket(ip, port)
                socket!!.soTimeout = receiverTimeOut
            } catch (e: Exception) {
                Logger.e("初始化TCP客户端失败！")
                e.printStackTrace()
                close()
                return
            }
            executors.execute(this)
        }
    }

    fun close() {
        lift = false
        Logger.i("TCP监听关闭")
        socket?.close()
        socket = null
    }

    fun send(message: String) {
        try {
            val sendBytes = message.toByteArray(Charsets.UTF_8)
            socket?.getOutputStream()?.use {
                it.write(sendBytes)
            }
            socket?.shutdownOutput()// 关闭输出流，关闭的流不能再打开了。
        } catch (e: Exception) {
            Logger.e("TCP发送数据失败！")
            e.printStackTrace()
        }
    }

    override fun run() {
        while (lift) {
            try {
                Logger.i("TCP监听中……")
                val sb = StringBuilder()
                socket?.getInputStream()?.buffered(receiverBufferSize).use {
                    while (it!!.available() > 0) {// 收到对方结束标记，即对方s.shutdownOutput()。循环结束
                        sb.append(it.read())
                    }
                }
                socket?.shutdownInput()// 关闭输入流，关闭的流不能再打开了。
                RxBus.post(RxBusTag.TAG_TCP_RECEIVE_SUCCESS, sb.toString())
                Logger.i("TCP接收到消息：$sb")
            } catch (e1: SocketTimeoutException) {
                Logger.w("TCP没有收到数据")
                e1.printStackTrace()
            } catch (e: Exception) {
                Logger.e("TCP接收数据失败！")
                e.printStackTrace()
            }
        }
        close()
    }

}