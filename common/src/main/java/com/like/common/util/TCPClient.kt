package com.like.common.util

import android.text.TextUtils
import com.like.logger.Logger
import com.like.rxbus.RxBus
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * socket TCP 接收、发送数据的客户端
 *
 * @param port              服务器端口号
 * @param readBufferSize    接收器的缓存大小，默认1024kb
 * @param readTimeOut       接收器每次读取数据的超时时长，默认3000毫秒，超时后就会放弃接收数据。
 */
class TCPClient(private val port: Int, private val readBufferSize: Int = 2048, private val readTimeOut: Int = 10000) {
    private val executors: ExecutorService by lazy {
        Executors.newCachedThreadPool()
    }

    private var ip: String = ""

    fun setIp(ip: String) {
        this.ip = ip
    }

    /**
     * 数组转换成十六进制字符串
     */
    private fun bytesToHexString(bArray: ByteArray): String {
        val sb = StringBuffer(bArray.size)
        var sTemp: String
        for (i in bArray.indices) {
            sTemp = Integer.toHexString(0xFF and bArray[i].toInt())
            if (sTemp.length < 2)
                sb.append(0)
            sb.append("${sTemp.toUpperCase()},")
        }
        return sb.toString()
    }

    fun send(message: Any) {
        if (TextUtils.isEmpty(ip) || port <= 0) {
            Logger.e("TCP发送消息失败，ip或者port无效")
            return
        }
        when (message) {
            is String -> Logger.i("TCP发送消息$message")
            is ByteArray -> Logger.i("TCP发送消息${bytesToHexString(message)}")
        }
        executors.execute {
            val buf = ByteArray(readBufferSize)
            var socket: Socket? = null
            var dos: DataOutputStream? = null
            var dis: DataInputStream? = null
            try {
                Logger.i("${Thread.currentThread().name}——初始化TCP客户端")
                socket = Socket(ip, port)
                socket.soTimeout = readTimeOut

                // 发送数据
                dos = DataOutputStream(socket.getOutputStream())
                when (message) {
                    is String -> dos.writeUTF(message)
                    is ByteArray -> dos.write(message)
                }
                dos.flush()

                Logger.i("${Thread.currentThread().name}——TCP发送数据成功")
                // 等待接收数据
                dis = DataInputStream(socket.getInputStream())
                Logger.i("${Thread.currentThread().name}——TCP监听返回数据中……")
                val length = dis.read(buf)
                if (length > 0) {
                    RxBus.post(RxBusTag.TAG_TCP_RECEIVE_SUCCESS, buf.copyOf(length))
                    Logger.i("${Thread.currentThread().name}——TCP接收到消息：${String(buf, 0, length, Charsets.UTF_8)}")
                }
                Logger.i("${Thread.currentThread().name}——TCP接收完毕")
            } catch (e: Exception) {
                Logger.e("${Thread.currentThread().name}——TCP客户端异常")
                e.printStackTrace()
            } finally {
                // 关闭资源
                dis?.close()
                dos?.close()
                socket?.close()
            }
        }
    }

    fun close() {
        executors.shutdownNow()
    }

}