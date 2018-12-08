package com.messy.pictorial

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.messy.delegate.MMap
import com.messy.pictorial.thread.ThreadPool
import com.messy.util.switch2All
import com.messy.util.switch2Debug
import org.litepal.LitePal

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Pictorial : Application() {

    override fun onCreate() {
        super.onCreate()
        MMap.init(this)
        LockPictorialNotification.init(this)
        if (isMainProcess()) {
            LitePal.initialize(this)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
        ThreadPool.getDefaultThreadPool().shutdownNow()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
        when (level) {
            TRIM_MEMORY_COMPLETE//内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
            -> {
            }
            TRIM_MEMORY_MODERATE//内存不足，并且该进程在后台进程列表的中部。
            -> {
            }
            TRIM_MEMORY_BACKGROUND//内存不足，并且该进程是后台进程。
            -> {
                NetworkClient.clear()
                ThreadPool.getDefaultThreadPool().shutdown()
            }
            TRIM_MEMORY_UI_HIDDEN//内存不足，并且该进程的UI已经不可见了。
            -> {
            }
            TRIM_MEMORY_RUNNING_CRITICAL//内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
            -> {
            }
            TRIM_MEMORY_RUNNING_LOW//内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
            -> {
            }
            TRIM_MEMORY_RUNNING_MODERATE//内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存.
            -> {
            }
        }
    }

    fun getCurrentProcessName(): String {
        val pid = android.os.Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (process in activityManager.runningAppProcesses) {
            if (process.pid == pid)
                return process.processName
        }
        return ""
    }

    fun isMainProcess(): Boolean {
        return packageName == getCurrentProcessName()
    }
}