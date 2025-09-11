package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val user_id: String,
    val username: String,
    val bio: String,
    val profile_image: String,
    val last_connected: String,
    val is_online: Boolean,
    val fcm_token: String = ""
)
