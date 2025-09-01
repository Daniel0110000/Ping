package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileData(
    val userId: String,
    val username: String,
    val bio: String,
    val profileImageName: String = "",
    val profileImagePath: String
)
