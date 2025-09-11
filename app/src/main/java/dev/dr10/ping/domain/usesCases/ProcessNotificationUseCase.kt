package dev.dr10.ping.domain.usesCases

import android.content.Context
import com.google.firebase.messaging.RemoteMessage
import dev.dr10.ping.android.notifications.AppNotificationsManager
import dev.dr10.ping.android.notifications.NotificationUtils
import dev.dr10.ping.data.mappers.toDataClass

class ProcessNotificationUseCase(
    private val appNotificationsManager: AppNotificationsManager
) {
    operator fun invoke(context: Context, message: RemoteMessage) {
        // Check if notification permissions are granted
        if (NotificationUtils.checkNotificationPermission(context)) {
            // If the permission is granted, display the notification
            val data = message.data.toDataClass()
            appNotificationsManager.sendChatNotification(context, data)
        }
    }
}