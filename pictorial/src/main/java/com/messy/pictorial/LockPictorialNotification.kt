package com.messy.pictorial

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.messy.util.string

class LockPictorialNotification private constructor() {
    companion object {
        const val LOCK_PIC_NOTIFICATION_ID = 31
        private const val CHANNEL_ID = "com.messy.pictorial.notification.channel"
        private lateinit var application: Application
        fun init(app: Application) {
            application = app
        }

        fun newInstance(): LockPictorialNotification {
            return LockPictorialNotification()
        }

        fun cancelNotification() {
            val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(LOCK_PIC_NOTIFICATION_ID)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = application.applicationContext.string(R.string.channel_name)
            val description = application.applicationContext.string(R.string.channel_desc)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            channel.description = description
            val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun create(): Notification {
        createNotificationChannel()
        val notificationBuilder = NotificationCompat.Builder(application.applicationContext, CHANNEL_ID)
        val contentTitle = application.applicationContext.string(R.string.channel_name)
        val contentText = application.applicationContext.string(R.string.channel_desc)
        notificationBuilder
            .setSmallIcon(R.drawable.divider)
            .setContentText(contentText)
            .setContentTitle(contentTitle)
            .setAutoCancel(false)
            .setOngoing(true)
        return notificationBuilder.build()
    }
}