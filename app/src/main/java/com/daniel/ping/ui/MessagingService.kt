package com.daniel.ping.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.daniel.ping.R
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.ui.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * A Firebase messaging service that extends FirebaseMessagingService to handle incoming FCM messages
 */
class MessagingService: FirebaseMessagingService() {

    /**
     * This method called when a new token is generated,
     * ... It can be used to save the token to a server or to update the user's profile with the new token
     * @param token The new token generated for the devices
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    /**
     * This method is called when a new message is received from the FCM server
     * @param message The RemoteMessage object containing the message data
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Extract data from the message payload
        val data = message.data
        // Display a notification with the message title and body
        showNotification(data["title"].toString(), data["body"].toString())

    }

    /**
     * Shows a notification with the specified title and text. The notification will have a default priority
     * If the notification channel does not exist, it will be created before displaying the notification
     * @param title The title of the notification
     * @param text The text of the notification
     */
    private fun showNotification(title: String, text: String){
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app)
            .setColor(ContextCompat.getColor(this, R.color.ultramarine_blue))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)

        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())

    }

    /**
     * Creates a notification channel for devices running Android Oreo (API level 26) or higher
     */
    private fun createNotificationChannel(){
        // Check if the device's Android version is Oreo (API level 26) or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification channel"
            val descriptionText = "Notification channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
            notificationManager.createNotificationChannel(channel)
        }
    }

}