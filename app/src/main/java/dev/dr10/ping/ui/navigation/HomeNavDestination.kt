package dev.dr10.ping.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomePlaceHolder

@Serializable
data object Network

@Serializable
data class Chat(val userData: String)