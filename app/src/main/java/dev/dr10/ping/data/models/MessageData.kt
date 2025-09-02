package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val id: Int = 0,
    val sender_id: String,
    val receiver_id: String,
    val content: String,
    val created_at: String = ""
)
