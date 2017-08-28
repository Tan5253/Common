package com.like.common.sample.socket.command

import java.nio.ByteBuffer
import java.util.*

/**
 * 通信返回的消息
 * 0xa5（消息头） + 消息发送模块号（2字节） + 消息接收模块号（2字节）+ 消息码（1字节）+ 消息净荷长度（2字节） + 消息净荷（最大2000字节）。
 */
class Message {
    /**
     * 消息发送模块号(2字节)
     */
    private var senderModuleId: Short = 0
    /**
     * 消息接收模块号(2字节)
     */
    private var receiverModuleId: Short = 0
    /**
     * 消息码(1字节)
     */
    private var code: Byte = 0
    /**
     * 消息净荷长度(2字节)
     */
    private var messageLength: Short = 0
    /**
     * 消息净荷(最大2000字节)
     */
    private val message: ByteArray by lazy {
        ByteArray(messageLength.toInt())
    }
    /**
     * 消息头(2字节)
     */
    private var header: Short = 0

    private val contentBuf: ByteBuffer = ByteBuffer.allocate(2048)

    /**
     * 解析数据
     */
    fun parse(data: ByteArray): Message {
        contentBuf.put(data)
        contentBuf.flip()
        header = contentBuf.short
        senderModuleId = contentBuf.short
        receiverModuleId = contentBuf.short
        code = contentBuf.get()
        messageLength = contentBuf.short
        contentBuf.get(message)
        contentBuf.clear()
        return this
    }

    override fun toString(): String {
        return "Message(senderModuleId=$senderModuleId, receiverModuleId=$receiverModuleId, code=$code, messageLength=$messageLength, message=${Arrays.toString(message)}, header=$header)"
    }

}


