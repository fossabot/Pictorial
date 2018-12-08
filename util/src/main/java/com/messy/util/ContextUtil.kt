@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.messy.util

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

inline val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

inline fun Context.toast(charSequence: CharSequence) {
    Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show()
}

inline fun Context.longToast(charSequence: CharSequence) {
    Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show()
}

inline fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.string(@StringRes id: Int, vararg args: Any?): String {
    return if (args.isEmpty())
        getString(id)
    else {
        getString(id, *args)
    }
}

inline fun Context.inflate(res: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false) =
    LayoutInflater.from(this).inflate(res, parent, attachToRoot)!!

fun Context.getFilePathByUri(uri: Uri) {
    getFilePathByUri(this, uri)
}

/*fun Context.isServiceRunning(service: String): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services = activityManager.runningAppProcesses ?: return false
    for (info in services) {
        if (info != null&&info.) {

        }
    }
}*/


