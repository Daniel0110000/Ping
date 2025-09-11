package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequestBody(
    val to: String = "",
    val content: String,
    val senderId: String,
    val username: String,
    val profileImageUrl: String
)
