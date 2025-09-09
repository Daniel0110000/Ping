package dev.dr10.ping.domain.models

data class UserStatusModel(
    val isOnline: Boolean,
    val lastConnected: String
)
