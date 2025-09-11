package dev.dr10.ping.data.remote

import dev.dr10.ping.BuildConfig
import dev.dr10.ping.data.models.NotificationRequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationsApiService {

    @Headers("Authorization: Bearer ${BuildConfig.SUPABASE_KEY}")
    @POST("/functions/v1/send-notification")
    suspend fun sendNotification(@Body notification: NotificationRequestBody)

}