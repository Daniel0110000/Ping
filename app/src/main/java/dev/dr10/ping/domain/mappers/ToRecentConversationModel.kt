package dev.dr10.ping.domain.mappers

import dev.dr10.ping.data.local.room.RecentConversationEntity
import dev.dr10.ping.domain.models.RecentConversationModel

fun RecentConversationEntity.toModel(): RecentConversationModel = RecentConversationModel(
    conversationId = conversationId,
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
    updatedAt = updatedAt
)