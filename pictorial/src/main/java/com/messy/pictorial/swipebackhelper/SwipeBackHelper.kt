package com.messy.pictorial.swipebackhelper

import android.app.Application
import android.view.MotionEvent

class SwipeBackHelper {
    companion object {
        private val lock = Any()
        private var lifecycleHelper: ActivityLifecycleHelper? = null
        private fun getLifecycleHelper(): ActivityLifecycleHelper {
            if (lifecycleHelper == null)
                synchronized(lock) {
                    if (lifecycleHelper == null)
                        lifecycleHelper = ActivityLifecycleHelper()
                }
            return lifecycleHelper!!
        }

        fun init(application: Application) {
            application.registerActivityLifecycleCallbacks(getLifecycleHelper())
        }
    }

    private val touchHelper = TouchHelper(getLifecycleHelper())

    var isFullScreen
        get() = touchHelper.isFullScreen
        set(value) {
            touchHelper.isFullScreen = value
        }

    fun progressTouchEvent(ev: MotionEvent): Boolean {
        return touchHelper.processTouchEventInternal(ev)
    }
}