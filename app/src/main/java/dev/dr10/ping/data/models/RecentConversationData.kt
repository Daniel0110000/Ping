package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RecentConversationData(
    val conversation_id: String,
    val sender_id: String,
    val receiver_id: String,
    val updated_at: String = ""
)
