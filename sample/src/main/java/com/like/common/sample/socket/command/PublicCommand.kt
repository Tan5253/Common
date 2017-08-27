package com.like.common.sample.socket.command

/**
 * 公共模块相关的内容
 */
class PublicCommand : Command(0x1506, 0x0700) {
    fun queryVersionInfo() = putData(0x03)// 版本信息查询
    fun updateSoundBoxSoft() = putData(0x05)// 启动软件升级
}
