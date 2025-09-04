package dev.dr10.ping.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SimpleUserData(
    val username: String,
    val profile_image: String
)
