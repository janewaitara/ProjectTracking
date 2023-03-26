package com.mumbicodes.projectie.domain.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mumbicodes.projectie.R
import com.mumbicodes.projectie.presentation.util.*

fun makeNotification(
    notificationType: NotificationType,
    notificationId: Int,
    message: String,
    context: Context,
) {

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

    val notificationTitle = when (notificationType) {
        NotificationType.PROJECTS -> PROJECTS_NOTIFICATION_TITLE
        NotificationType.MILESTONES -> MILESTONES_NOTIFICATION_TITLE
    }

    // Make notification
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(notificationTitle)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .build()

    // Each notification has an it's own unique ID
    NotificationManagerCompat.from(context).notify(notificationId, notification)
}
enum class NotificationType {
    PROJECTS, MILESTONES
}
