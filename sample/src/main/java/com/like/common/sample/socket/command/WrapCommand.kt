package com.like.common.sample.socket.command

class WrapCommand(val command: Command) {
    var time = -1L
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WrapCommand

        if (time != other.time) return false

        return true
    }

    override fun hashCode(): Int {
        return time.hashCode()
    }


}
