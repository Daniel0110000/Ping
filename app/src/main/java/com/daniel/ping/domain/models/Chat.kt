package com.daniel.ping.domain.models

import java.util.Date

data class Chat(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val dateTime: String,
    val dateObject: Date,
    val conversationId: String = "",
    val conversationName: String = "",
    val conversationImage: String = ""
)
