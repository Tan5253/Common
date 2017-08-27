package com.like.common.sample.socket.command

/**
 * 通信返回的消息
 * 0xa5（消息头） + 消息发送模块号（2字节） + 消息接收模块号（2字节）+ 消息码（1字节）+ 消息净荷长度（2字节） + 消息净荷（最大2000字节）。
 */
class Message(
        /**
         * 消息发送模块号(2字节)
         */
        var senderModuleId: Short,
        /**
         * 消息接收模块号(2字节)
         */
        var receiverModuleId: Short,
        /**
         * 消息码(1字节)
         */
        var code: Byte,
        /**
         * 消息净荷长度(2字节)
         */
        var messageLength: Short,
        /**
         * 消息净荷(最大2000字节)
         */
        var message: ByteArray,
        /**
         * 消息头(2字节)
         */
        val header: Short = 0xa5
)


