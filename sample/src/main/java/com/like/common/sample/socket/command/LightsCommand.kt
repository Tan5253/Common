package com.like.common.sample.socket.command

/**
 * 灯相关的命令
 */
class LightsCommand : Command(0x1500, 0x0e00) {
    fun open() = putData(0x50, byteArrayOf(0x01))// 打开
    fun close() = putData(0x50, byteArrayOf(0x00))// 关闭
    fun increaseBrightness() = putData(0x51, byteArrayOf(0x01))// 增加亮度
    fun reduceBrightness() = putData(0x51, byteArrayOf(0x02))// 减小亮度
    fun color() = putData(0x53)// 设置灯颜色
}