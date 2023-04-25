package com.daniel.ping.data.models

data class PushNotification(
    val data: NotificationData,
    val to: String? = ""
)
