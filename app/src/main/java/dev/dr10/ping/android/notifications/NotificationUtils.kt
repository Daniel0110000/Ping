package dev.dr10.ping.android.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import dev.dr10.ping.domain.mappers.encodeToString
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.ui.MainActivity
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object NotificationUtils {

    /**
     * Check if the app has notification permission (for Android 13 and above)
     *
     * @param context The context to use for checking the permission
     * @return True if the permission is granted or not required, false otherwise
     */
    fun checkNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    /**
     * Build a [PendingIntent] to open chat screen when notification is tapped
     *
     * @param context The context to use for creating the PendingIntent
     * @param userData The user data to pass to the chat screen
     * @return The created [PendingIntent]
     */
    fun buildChatContentIntent(
        context: Context,
        userData: UserProfileModel
    ): PendingIntent {
        val json = userData.encodeToString()
        val encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = "${Constants.NOTIFICATION_DEEP_LINK_BASE_URL}$encodedJson".toUri()
        }

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }!!
    }

    /**
     * Build a PendingIntent to handle notification dismiss action
     *
     * @param context The context to use for creating the PendingIntent
     * @param userId The user ID associated with the notification
     * @return The created [PendingIntent]
     */
    fun buildNotificationDismissIntent(context: Context, userId: String): PendingIntent {
        val deleteIntent = Intent(context, NotificationDismissReceiver::class.java).apply {
            putExtra(Constants.USER_PROFILE_ID, userId)
        }
        return PendingIntent.getBroadcast(
            context,
            userId.hashCode(),
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Create notification channel for Android O and above
     *
     * @param context The context to use for creating the channel
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.NOTIFICATIONS_CHANNEL_ID,
                Constants.NOTIFICATIONS_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Load user profile image from URL and convert it to [Bitmap]
     *
     * @param context The context to use for loading the image
     * @param url The URL of the image to load
     * @return The loaded [Bitmap], or null if loading failed
     */
    suspend fun loadUserProfileBitmap(context: Context, url: String): Bitmap? = try {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()
        val result = (loader.execute(request) as? SuccessResult)?.drawable
        (result as? BitmapDrawable)?.bitmap
    } catch (_: Exception) { null }
}