package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val user_id: String,
    val username: String,
    val bio: String,
    val profile_image: String,
    val fcm_token: String = ""
)
