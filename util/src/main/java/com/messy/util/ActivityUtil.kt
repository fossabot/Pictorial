@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.messy.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager

var Activity.backgroundAlpha: Float
    get() = window.attributes.alpha
    set(value) {
        val lp = window.attributes
        lp.alpha = value
        if (value == 1f)
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        else
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }

inline fun Activity.lightStatusBar(light: Boolean) {
    if (light) lightStatusBar()
    else darkStatusBar()
}

fun Activity.lightStatusBar() {
    var flag = window.decorView.systemUiVisibility
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    window.decorView.systemUiVisibility = flag
}

fun Activity.darkStatusBar() {
    var flag = window.decorView.systemUiVisibility
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
    window.decorView.systemUiVisibility = flag
}

fun Activity.backToHome() {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        addCategory(Intent.CATEGORY_HOME)
    }
    startActivity(intent)
}

fun Activity.exitToHome() {
    backToHome()
    android.os.Process.killProcess(android.os.Process.myPid())
}

fun Activity.setTranslateFlag() {
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
}

fun Activity.setFullScreen() {
    /*window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)*/
    window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
}

fun Activity.restoreScreen() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
