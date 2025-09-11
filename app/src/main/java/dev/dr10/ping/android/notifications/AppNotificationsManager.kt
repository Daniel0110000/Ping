package dev.dr10.ping.android.notifications

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import dev.dr10.ping.R
import dev.dr10.ping.data.models.NotificationRequestBody
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppNotificationsManager {

    private val notificationStyles = mutableMapOf<String, NotificationCompat.MessagingStyle>()
    private val userInfo = mutableMapOf<String, Pair<String, Bitmap>>()

    /**
     * Send a chat notification
     *
     * @param context Application context
     * @param notificationData Data for the notification
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun sendChatNotification(
        context: Context,
        notificationData: NotificationRequestBody
    ) {
        // Create notification channel if not exists
        NotificationUtils.createNotificationChannel(context)

        CoroutineScope(Dispatchers.Default).launch {
            // Load user profile image
            val profileImage = NotificationUtils.loadUserProfileBitmap(context, notificationData.profileImageUrl)
            // Create sender user for notification style
            val senderUser = Person.Builder()
                .setName(notificationData.username)
                .setKey(notificationData.senderId)
                .apply { profileImage?.let { setIcon(IconCompat.createWithBitmap(it)) } }
                .build()

            // Get existing notification style or create a new one
            val existingStyle = notificationStyles[notificationData.senderId]
            val notificationStyle = if (existingStyle != null) {
                existingStyle.addMessage(notificationData.content, System.currentTimeMillis(), senderUser)
                existingStyle
            } else {
                val newStyle = NotificationCompat.MessagingStyle(senderUser)
                    .setConversationTitle(notificationData.username)
                    .addMessage(notificationData.content, System.currentTimeMillis(), senderUser)
                notificationStyles[notificationData.senderId] = newStyle
                userInfo[notificationData.senderId] = Pair(notificationData.username, profileImage!!)
                newStyle
            }

            // Build the notification with all necessary properties
            val notificationBuilder = NotificationCompat.Builder(context, Constants.NOTIFICATIONS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app)
                .setStyle(notificationStyle)
                .setAutoCancel(true)
                .setContentIntent(NotificationUtils.buildChatContentIntent(
                    context,
                    UserProfileModel(
                        userId = notificationData.senderId,
                        username = notificationData.username,
                        profileImageUrl = notificationData.profileImageUrl
                    )
                ))
                .setDeleteIntent(NotificationUtils.buildNotificationDismissIntent(context, notificationData.senderId))
            // Display the notification
            NotificationManagerCompat.from(context).notify(notificationData.senderId.hashCode(), notificationBuilder.build())
        }
    }

    /**
     * Remove notification data for a specific user
     *
     * @param userId The ID of the user whose notification data should be removed
     */
    fun removeNotificationForUser(userId: String) {
        Log.e("sss", notificationStyles.toString())
        notificationStyles.remove(userId)
        userInfo.remove(userId)
    }

    /**
     * Clear all stored notification data
     */
    fun clearAllNotificationsData() {
        notificationStyles.clear()
        userInfo.clear()
    }

    /**
     * Clear all displayed notifications from the notification tray and clear all stored notification data
     *
     * @param context Application context
     */
    fun clearAllDisplayedNotifications(context: Context) {
        NotificationManagerCompat.from(context).cancelAll()
        clearAllNotificationsData()
    }

}