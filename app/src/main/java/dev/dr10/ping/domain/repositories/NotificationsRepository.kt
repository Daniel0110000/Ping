package dev.dr10.ping.domain.repositories

import dev.dr10.ping.data.models.NotificationRequestBody

interface NotificationsRepository {

    suspend fun sendNotification(body: NotificationRequestBody)

}