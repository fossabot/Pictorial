@file:Suppress("unused")

package com.messy.util

import android.content.Context
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment

val metrics by lazy { DisplayMetrics() }
val Context.displayMetrics: DisplayMetrics
    get() = run {
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics
    }

val Context.displayWidth: Int
    get() = run {
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.widthPixels
    }

val Context.displayHeight: Int
    get() = run {
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.heightPixels
    }

val Context.statusBarHeight: Int
    get() {
        val id = applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (id > 0)
            applicationContext.resources.getDimensionPixelSize(id) else Dp2Px(25)
    }

val Fragment.statusBarHeight: Int
    get() = context!!.statusBarHeight

val Context.navigationBarHeight: Int
    get() {
        val id = applicationContext.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (id > 0)
            applicationContext.resources.getDimensionPixelSize(id) else Dp2Px(25)
    }

val Fragment.navigationBarHeight: Int
    get() = context!!.navigationBarHeight
