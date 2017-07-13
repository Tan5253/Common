package com.like.common.sample

import kotlin.concurrent.thread

@Volatile var a: Boolean = false

fun main(args: Array<String>) {
    test()
    test()
    test()
}

fun test() {
    println("调用了test方法")
    thread {
        if (!a) {
            synchronized(Thread::class) {
                if (!a) {
                    println("${Thread.currentThread().name} $a")
                    a = true
                    println("${Thread.currentThread().name} $a")
                }
            }
        }
    }
}