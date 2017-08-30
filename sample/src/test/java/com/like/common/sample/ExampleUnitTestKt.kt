package com.like.common.sample

import org.junit.Test

class ExampleUnitTestKt {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        val a: Byte = 0x15.toByte()
        val b: Int = a.toInt() and 0xff
        println(b == 0x15)
    }
}
