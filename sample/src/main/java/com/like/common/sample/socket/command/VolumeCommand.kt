package com.like.common.sample.socket.command

/**
 * 声音相关的命令
 */
class VolumeCommand : Command(0x1500, 0x1000) {
    fun increaseVolume() = putData(0x10, byteArrayOf(0x01))// 增加音量
    fun reduceVolume() = putData(0x10, byteArrayOf(0x02))// 减小音量
}