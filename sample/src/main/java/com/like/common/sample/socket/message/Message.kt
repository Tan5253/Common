package com.like.common.sample.socket.message

import com.like.common.sample.socket.command.Command
import com.like.common.util.HexUtil
import com.like.logger.Logger
import java.nio.ByteBuffer

/**
 * 通信返回的消息
 * 0xa5（消息头） + 消息发送模块号（2字节） + 消息接收模块号（2字节）+ 消息码（1字节）+ 消息净荷长度（2字节） + 消息净荷（最大2000字节）。
 */
class Message {
    /**
     * 消息头(1字节)
     */
    private var header: Byte = 0
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
    lateinit var message: ByteArray

    /**
     * 解析数据
     */
    fun parse(data: ByteArray): Message {
        Logger.d("解析Message之前：${HexUtil.encodeHexStr(data)}")
        val contentBuf: ByteBuffer = ByteBuffer.allocate(2048)
        contentBuf.put(data)
        contentBuf.flip()
        header = contentBuf.get()
        senderModuleId = contentBuf.short
        receiverModuleId = contentBuf.short
        code = contentBuf.get()
        messageLength = contentBuf.short
        message = ByteArray(messageLength.toInt())
        contentBuf.get(message)
        contentBuf.clear()
        return this
    }

    /**
     * 是否是正确的返回消息
     */
    fun isRightMessage(command: Command): Boolean {
        val isHeaderRight = header.toInt() and 0xff == 0xa5
        val isReceiverModuleIdRight = receiverModuleId == command.senderModuleId
        val isSenderModuleIdRight = senderModuleId == command.receiverModuleId
        return isHeaderRight && isReceiverModuleIdRight && isSenderModuleIdRight
    }

    override fun toString(): String {
        return "Message(header=$header, senderModuleId=$senderModuleId, receiverModuleId=$receiverModuleId, code=$code, messageLength=$messageLength, message=$message)"
    }

}


