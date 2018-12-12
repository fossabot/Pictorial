package com.messy.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Context.checkCanReadAndWrite(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkCanReceiveBootCompleted(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.RECEIVE_BOOT_COMPLETED
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkCanShowOnLockScreen(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.DISABLE_KEYGUARD
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkInternetPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.INTERNET
    ) == PackageManager.PERMISSION_GRANTED
}


fun Context.checkCanBackgroundRun(): Boolean {
    return true
}