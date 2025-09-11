package dev.dr10.ping.data.repositories

import dev.dr10.ping.data.models.NotificationRequestBody
import dev.dr10.ping.data.remote.NotificationsApiService
import dev.dr10.ping.domain.repositories.NotificationsRepository

class NotificationsRepositoryImpl(
    private val notificationsApiService: NotificationsApiService
): NotificationsRepository {

    /**
     * Send a notification using the Notifications API service
     *
     * @param body The notification request body containing the notification details
     */
    override suspend fun sendNotification(body: NotificationRequestBody) {
        notificationsApiService.sendNotification(body)
    }
}