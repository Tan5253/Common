package com.like.common.util

object ByteUtils {
    /**
     * 将int数值转换为byte数组。(低位在前，高位在后)
     *
     * @param value 要转换的int值
     * @param byteLength 要转换成几个字节的数组（1-4），默认4。
     * @return byte数组
     */
    fun intToBytes(value: Int, byteLength: Int = 4): ByteArray {
        val realLength = when {
            byteLength < 0 -> 0
            byteLength > 4 -> 4
            else -> byteLength
        }
        val result = ByteArray(realLength)
        (realLength - 1 downTo 0).forEach {
            result[it] = (value shr (8 * it) and 0xFF).toByte()
        }
        return result
    }

    fun int2Byte(data: Int) = (data and 0xFF).toByte()

    fun byte2Int(byte: Byte) = byte.toInt() and 0xFF

    /**
     * byte数组转换为int值。(低位在前，高位在后)，最多取4个字节。
     *
     * @param bytes 要转换的byte数组
     */
    fun bytes2Int(bytes: ByteArray): Int {
        var result = 0
        val length = if (bytes.size > 4) 4 else bytes.size
        (0 until length).forEach {
            result = result or (byte2Int(bytes[it]) shl (8 * it))
        }
        return result
    }

}
