package com.messy.pictorial.mvvm

import android.os.SystemClock
import androidx.lifecycle.LiveData

class TimerLiveData<T>(
    callback: () -> T,
    @Suppress("MemberVisibilityCanBePrivate")
    var waitMs: Long = 1000L
) : LiveData<T>() {

    private var myThread: Thread? = null

    @Volatile
    private var isPaused = false

    private val timer = Runnable {
        try {
            while (myThread?.isInterrupted != true) {
                if (isPaused) {
                    Thread.yield()
                } else {
                    postValue(callback())
                    SystemClock.sleep(waitMs)
                }
            }
        } catch (e: InterruptedException) {

        }
    }

    override fun onActive() {
        super.onActive()
        start()
    }

    override fun onInactive() {
        super.onInactive()
        stop()
    }

    private fun start() {
        resume()
        myThread = Thread(timer)
        myThread?.start()
    }

    fun restart() {
        stop()
        start()
    }

    fun stop() {
        myThread?.interrupt()
        myThread = null
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }
}