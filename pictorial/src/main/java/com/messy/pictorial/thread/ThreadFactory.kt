package com.messy.pictorial.thread

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class ThreadFactory : ThreadFactory {
    private val count = AtomicInteger(0)
    override fun newThread(r: Runnable?): Thread {
        val thread = Thread(r, "thread_${count.incrementAndGet()}")
        thread.priority = Thread.NORM_PRIORITY
        return thread
    }
}