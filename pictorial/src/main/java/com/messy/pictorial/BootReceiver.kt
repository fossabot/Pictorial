package com.messy.pictorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.messy.util.string


class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action || context.string(R.string.ACTION_USER) == intent.action) {
            try {
                LockService.startLockService(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}