package com.like.common.sample

import com.like.common.sample.socket.command.Message
import org.junit.Test
import java.nio.ByteBuffer

class ExampleUnitTestKt {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        val contentBuf: ByteBuffer = ByteBuffer.allocate(2048)
        contentBuf.putShort(0xa5)
        contentBuf.putShort(0x1000)
        contentBuf.putShort(0x1500)
        contentBuf.put(0x12)
        contentBuf.putShort(0x03)
        contentBuf.put(byteArrayOf(0x01, 0x02, 0x03))
        val result = ByteArray(contentBuf.position())
        contentBuf.flip()
        contentBuf.get(result)
        contentBuf.clear()
        val msg: Message = Message()
        msg.parse(result)
        println(msg)
    }
}
