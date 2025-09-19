package dev.dr10.ping.domain.mappers

import dev.dr10.ping.data.local.room.RecentConversationEntity
import dev.dr10.ping.data.models.RecentConversationData
import dev.dr10.ping.data.models.SimpleUserData
import dev.dr10.ping.domain.extensions.toProfileImageUrl

fun RecentConversationData.toRecentConversationEntity(
    userId: String,
    userData: SimpleUserData
): RecentConversationEntity = RecentConversationEntity(
    conversationId = this.conversation_id,
    userId = userId,
    username = userData.username,
    profilePictureUrl = userData.profile_image.toProfileImageUrl(),
    updatedAt = this.updated_at
)