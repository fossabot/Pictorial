@file:Suppress("unused")

package com.messy.util

import android.util.Log

private const val WTF = 0
private const val VERBOSE = 1
private const val DEBUG = 2
private const val INFO = 3
private const val WARN = 4
private const val ERROR = 5
private const val ASSERT = 6
private const val NOTHING = 7
private const val ALL = 99

private var level = NOTHING


private fun check(lv: Int): Boolean {
    return lv <= level
}

fun isDebug(): Boolean {
    return level == DEBUG
}

fun Any.logd(msg: String, e: Throwable? = null) {
    if (check(DEBUG)) {
        val tag = this.javaClass.simpleName
        if (e != null)
            Log.d(tag, msg, e)
        else
            Log.d(tag, msg)
    }
}

fun Any.logv(msg: String, e: Throwable? = null) {
    if (check(VERBOSE)) {
        val tag = this.javaClass.simpleName
        if (e != null)
            Log.v(tag, msg, e)
        else
            Log.v(tag, msg)
    }
}

fun Any.logi(msg: String, e: Throwable? = null) {
    if (check(INFO)) {
        val tag = this.javaClass.simpleName
        if (e != null)
            Log.i(tag, msg, e)
        else
            Log.i(tag, msg)
    }
}

fun Any.logw(msg: String, e: Throwable? = null) {
    if (check(WARN)) {
        val tag = this.javaClass.simpleName
        if (e != null)
            Log.w(tag, msg, e)
        else
            Log.w(tag, msg)
    }
}

fun Any.loge(msg: String, e: Throwable? = null) {
    if (check(ERROR)) {
        val tag = this.javaClass.simpleName
        if (e != null)
            Log.e(tag, msg, e)
        else
            Log.e(tag, msg)
    }
}

fun Any.logwtf(msg: String, e: Throwable? = null) {
    if (check(WTF)) {
        val tag = this.javaClass.simpleName
        if (e != null)
            Log.wtf(tag, msg, e)
        else
            Log.wtf(tag, msg)
    }
}

fun Any.logwtf(e: Throwable? = null) {
    if (check(WTF)) {
        val tag = this.javaClass.simpleName
        Log.wtf(tag, e)
    }
}

fun switch2All() {
    level = ALL
}

fun switch2NONE() {
    level = NOTHING
}

fun switch2Verbose() {
    level = VERBOSE
}

fun switch2Debug() {
    level = DEBUG
}

fun switch2Info() {
    level = INFO
}

fun switch2Warn() {
    level = WARN
}

fun switch2Error() {
    level = ERROR
}
