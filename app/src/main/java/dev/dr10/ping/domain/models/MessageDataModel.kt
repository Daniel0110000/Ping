package dev.dr10.ping.domain.models

data class MessageDataModel(
    val senderId: String,
    val receiverId: String,
    val receiverUsername: String,
    val message: String,
    val date: String
)
