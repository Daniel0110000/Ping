package com.daniel.ping.domain.models

import java.util.Date

data class RecentConversation(
    val senderId: String,
    val receiverId: String,
    val profileImageUrl: String,
    val name: String,
    val description: String,
    val token: String,
    var dateObject: Date
)