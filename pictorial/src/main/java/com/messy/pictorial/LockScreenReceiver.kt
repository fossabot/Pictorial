package com.messy.pictorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LockScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_SCREEN_OFF == intent.action) {
            val lockScreen = Intent(context, LockScreenActivity::class.java)
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(lockScreen)
        }
    }
}
