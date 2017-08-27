package com.like.common.sample.socket.command

import java.nio.ByteBuffer

/**
 * 蓝牙通信的命令，这里定义每条命令只代表一个命令（暂时不需要代表多个命令）
 *
 * 0x7e（命令头） + 命令发送模块号（2字节） + 命令接收模块号（2字节） + 命令条数（1字节）+命令码1（1字节）+ 参数长度1（2字节）+ 参数净荷区1+ 命令码2（1字节）+ 参数长度2（2字节）+ 参数净荷区2......。
 *
 * @param senderModuleId    命令发送模块号(2字节)
 * @param receiverModuleId  命令接收模块号(2字节)
 */
open class Command(private val senderModuleId: Short, private val receiverModuleId: Short) {
    // 命令头(1字节)
    private val header: Byte = 0x7e
    // 命令条数(1字节)，默认只发1条
    private val cmdNumber: Byte = 0x01
    // 完整命令的缓存
    private val contentBuf: ByteBuffer = ByteBuffer.allocate(2048)

    /**
     * 放入命令码和参数，并返回一条完整的命令数据
     *
     * @param code      命令码(1字节)
     * @param parameter 参数净荷区
     */
    protected fun putData(code: Byte, parameter: ByteArray = byteArrayOf()): ByteArray {
        contentBuf.put(header)
        contentBuf.putShort(senderModuleId)
        contentBuf.putShort(receiverModuleId)
        contentBuf.put(cmdNumber)
        contentBuf.put(code)
        contentBuf.putShort(parameter.size.toShort())
        contentBuf.put(parameter)
        val result = ByteArray(contentBuf.position())
        contentBuf.flip()
        contentBuf.get(result)
        contentBuf.clear()
        return result
    }
}


