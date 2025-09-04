package dev.dr10.ping.domain.models

data class RecentConversationModel(
    val conversationId: String,
    val userId: String,
    val username: String,
    val profilePictureUrl: String,
    val updatedAt: String
)
