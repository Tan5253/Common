package com.like.common.sample.socket.client

import android.text.TextUtils
import com.like.common.sample.socket.command.WrapCommand
import com.like.common.sample.socket.message.Message
import com.like.common.sample.socket.message.WrapMessage
import com.like.common.util.HexUtil
import com.like.common.util.RxBusTag
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

    fun send(cotent: Any) {
        if (TextUtils.isEmpty(ip) || port <= 0) {
            Logger.e("TCP发送消息失败，ip或者port无效")
            return
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
                when (cotent) {
                    is String -> dos.writeUTF(cotent.apply { Logger.i("TCP发送消息$this") })
                    is WrapCommand -> dos.write(cotent.command.getFullCommand().apply { Logger.i("TCP发送消息${HexUtil.encodeHexStr(this)}") })
                }
                dos.flush()

                Logger.i("${Thread.currentThread().name}——TCP发送数据成功")
                // 等待接收数据
                dis = DataInputStream(socket.getInputStream())
                Logger.i("${Thread.currentThread().name}——TCP监听返回数据中……")
                val length = dis.read(buf)
                if (length > 0) {
                    RxBus.post(RxBusTag.TAG_TCP_RECEIVE_SUCCESS, WrapMessage(Message().parse(buf.copyOf(length))).apply { time = (cotent as WrapCommand).time })
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