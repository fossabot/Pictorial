package com.messy.pictorial.thread

import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class ThreadPool private constructor() {
    companion object {
        private val CPU_COUNT get() = Runtime.getRuntime().availableProcessors()
        private val CORE_POOL_SIZE get() = max(2, min(CPU_COUNT, 4))
        private val MAX_POOL_SIZE get() = CPU_COUNT * 4 + 1
        private val DEFAULT_UNIT = TimeUnit.SECONDS
        private const val DEFAULT_KEEP_ALIVE_TIME = 30L
        private var default: ThreadPool? = null
        private val lock = Any()

        fun getDefaultThreadPool(): ThreadPool {
            if (default == null) {
                synchronized(lock) {
                    if (default == null)
                        default = ThreadPool()
                }
            }
            return default!!
        }
    }

    private val workQueue = SynchronousQueue<Runnable>()
    private val rejectedExecutionHandler = ThreadPoolExecutor.DiscardOldestPolicy()
    private val threadFactory = ThreadFactory()
    private val executor = ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAX_POOL_SIZE,
        DEFAULT_KEEP_ALIVE_TIME,
        DEFAULT_UNIT,
        workQueue,
        threadFactory,
        rejectedExecutionHandler
    )

    fun execute(task: Runnable) {
        executor.execute(task)
    }

    fun execute(task: () -> Unit) {
        executor.execute(task)
    }

    fun shutdown() {
        executor.shutdown()
    }

    fun shutdownNow() {
        executor.shutdownNow()
    }
}