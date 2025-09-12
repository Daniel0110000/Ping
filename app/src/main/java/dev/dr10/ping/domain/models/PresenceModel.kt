package dev.dr10.ping.domain.models

data class PresenceModel(
    val userId: String,
    val isOnline: Boolean,
    val lastConnected: String
)
