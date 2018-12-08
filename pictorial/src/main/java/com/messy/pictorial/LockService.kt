package com.messy.pictorial

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import com.jeremyliao.livedatabus.LiveDataBus
import com.messy.util.string

class LockService : Service() {
    companion object {
        fun startLockService(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(Intent(context, LockService::class.java))
            else
                context.startService(Intent(context, LockService::class.java))
        }

        fun stopLockService(context: Context) {
            LockPictorialNotification.cancelNotification()
            context.stopService(Intent(context, LockService::class.java))
        }
    }

    private lateinit var receiver: LockScreenReceiver

    override fun onCreate() {
        super.onCreate()
        if (Settings.getInstance().isForegroundService) {
            startForeground(
                LockPictorialNotification.LOCK_PIC_NOTIFICATION_ID,
                LockPictorialNotification.newInstance().create()
            )
        }
        receiver = LockScreenReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(receiver, filter)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        applicationContext.sendBroadcast(Intent(applicationContext.string(R.string.ACTION_USER)))
        LiveDataBus.get().with("all", String::class.java).postValue("lock service is destroy")
    }
}
