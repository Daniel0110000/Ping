package dev.dr10.ping.domain.models

data class MessageModel(
    val senderId: String,
    val receiverId: String,
    val content: String,
    val date: String
)
