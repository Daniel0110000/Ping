package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PresenceData(
    val user_id: String,
    val is_online: Boolean,
    val last_connected: String
)
