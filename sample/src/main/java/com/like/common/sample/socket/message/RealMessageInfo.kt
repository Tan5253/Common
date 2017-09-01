package com.like.common.sample.socket.message

import java.nio.ByteBuffer

class RealMessageInfo {
    var code: Byte = 0// 0：成功；其它：失败；
    var data: String = ""// 真正的数据

    fun parse(dataBytes: ByteArray): RealMessageInfo {
        val contentBuf: ByteBuffer = ByteBuffer.allocate(2048)
        contentBuf.put(dataBytes)
        contentBuf.flip()
        code = contentBuf.get()
        val buf = ByteArray(dataBytes.size - 1)
        contentBuf.get(buf)
        contentBuf.clear()
        data = String(buf, 0, buf.size, Charsets.UTF_8)
        return this
    }

    override fun toString(): String {
        return "RealMessageInfo(code=$code, data='$data')"
    }

}
