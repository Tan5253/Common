package com.like.common.util

import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.Executors

/**
 * socket TCP 接收、发送数据的客户端
 *
 * @param port              服务器端口号
 * @param readBufferSize    接收器的缓存大小，默认1024kb
 * @param readTimeOut       接收器每次读取数据的超时时长，默认3000毫秒，超时后就会放弃接收数据。
 */
class TCPClient(val port: Int, val readBufferSize: Int = 1024, val readTimeOut: Int = 3000) {
    private val executors = Executors.newCachedThreadPool()
    var ip: String = ""

    fun send(message: String) {
        executors.execute {
            try {
                Logger.i("${Thread.currentThread().name}——初始化TCP客户端")
                val socket = Socket(ip, port)
                socket.soTimeout = readTimeOut
                val dos = DataOutputStream(socket.getOutputStream())
                val dis = DataInputStream(socket.getInputStream())
                val buf = ByteArray(readBufferSize)
                // 发送数据
                dos.writeUTF(message)
                dos.flush()
                Logger.i("${Thread.currentThread().name}——TCP发送数据成功")
                // 等待接收数据
                Logger.i("${Thread.currentThread().name}——TCP监听返回数据中……")
                val length = dis.read(buf)
                if (length > 0) {
                    val msgRcv = String(buf, 0, length, Charsets.UTF_8)
                    RxBus.post(RxBusTag.TAG_TCP_RECEIVE_SUCCESS, msgRcv)
                    Logger.i("${Thread.currentThread().name}——TCP接收到消息：$msgRcv")
                }
                // 关闭资源
                dis.close()
                dos.close()
                socket.close()
            } catch (e: Exception) {
                Logger.e("${Thread.currentThread().name}——TCP客户端异常")
                e.printStackTrace()
            }
        }
    }

}