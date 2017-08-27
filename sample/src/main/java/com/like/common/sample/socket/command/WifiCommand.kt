package com.like.common.sample.socket.command

/**
 * Wifi相关的命令
 */
class WifiCommand : Command(0x1507, 0x0b00) {
    fun open() = putData(0x20, byteArrayOf(0x01))// 打开wifi
    fun close() = putData(0x20, byteArrayOf(0x00))// 关闭wifi
    fun setName(name: String) = putData(0x21, name.toByteArray())// 设置wifi名称
    fun setPassword(password: String) = putData(0x22, password.toByteArray())// 设置wifi密码
    fun scan() = putData(0x23)// 扫描wifi
}
