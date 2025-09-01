package dev.dr10.ping.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileModel(
    val userId: String,
    val username: String,
    val bio: String,
    val profileImageUrl: String = ""
)
