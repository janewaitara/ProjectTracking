package com.mumbicodes.projectie.domain.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mumbicodes.projectie.presentation.util.CHANNEL_ID
import com.mumbicodes.projectie.presentation.util.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.mumbicodes.projectie.presentation.util.VERBOSE_NOTIFICATION_CHANNEL_NAME

fun makeNotification(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Notification Channel
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                VERBOSE_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        // Add the channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }
}
