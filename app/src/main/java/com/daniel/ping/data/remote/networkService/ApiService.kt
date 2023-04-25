package com.daniel.ping.data.remote.networkService

import com.daniel.ping.BuildConfig
import com.daniel.ping.data.models.PushNotification
import com.daniel.ping.domain.utilities.Constants.PATH_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Interface for making API requests to send push notifications
 */
interface ApiService {

    /**
     * Sends a push notification using the FCM API
     * @param notification The PushNotification object containing the notification data
     * @return A Response object containing the result of the API request
     */
    @Headers(
        "Content-Type: application/json",
        "Authorization: key=${BuildConfig.API_KEY}"
    )
    @POST(PATH_URL)
    suspend fun sendNotification(@Body notification: PushNotification): Response<PushNotification>

}