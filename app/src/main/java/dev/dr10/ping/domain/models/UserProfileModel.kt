package dev.dr10.ping.domain.models

data class UserProfileModel(
    val userId: String,
    val username: String,
    val bio: String,
    val profileImageUrl: String = ""
)
