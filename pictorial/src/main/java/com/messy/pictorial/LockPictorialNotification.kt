package com.messy.pictorial

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.messy.util.string

class LockPictorialNotification private constructor() {
    companion object {
        const val LOCK_PIC_NOTIFICATION_ID = 31
        private const val CHANNEL_ID = "com.messy.pictorial.notification.channel"
        private lateinit var application: Application
        private var isCreatedChannel = false
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
        if (isCreatedChannel)
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = application.applicationContext.string(R.string.channel_name)
            val description = application.applicationContext.string(R.string.channel_desc)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            channel.description = description
            val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            isCreatedChannel = true
        }
    }

    fun create(): Notification {
        createNotificationChannel()
        val notificationBuilder = NotificationCompat.Builder(application.applicationContext, CHANNEL_ID)
        val contentTitle = application.applicationContext.string(R.string.lock_notification_content_title)
        val contentText = application.applicationContext.string(R.string.lock_notification_content_text)
        val pendingIntent = PendingIntent.getActivity(application, 0, Intent(application, MainActivity::class.java), 0)
        notificationBuilder
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(contentText)
            .setContentTitle(contentTitle)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
        return notificationBuilder.build()
    }
}