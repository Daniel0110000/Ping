package dev.dr10.ping.android.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.dr10.ping.domain.utils.Constants
import org.koin.java.KoinJavaComponent.inject

class NotificationDismissReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val appNotificationsManager: AppNotificationsManager by inject(AppNotificationsManager::class.java)
        // Get userId from intent extras, if not found return
        val userId = intent.getStringExtra(Constants.USER_PROFILE_ID) ?: return
        // Remove the notification data for the userId
        appNotificationsManager.removeNotificationForUser(userId)
    }
}