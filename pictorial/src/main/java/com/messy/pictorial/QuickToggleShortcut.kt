package com.messy.pictorial

import android.app.Activity
import android.os.Bundle
import com.messy.util.string
import com.messy.util.toast

class QuickToggleShortcut : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Settings.getInstance().isForegroundService) {
            toast(string(R.string.stop_lock_service))
            Settings.getInstance().isForegroundService = false
            LockService.stopLockService(this)
        } else {
            toast(string(R.string.start_lock_service))
            Settings.getInstance().isForegroundService = true
            LockService.startLockService(this)
        }
        finish()
    }
}

