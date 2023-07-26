package com.daniel.ping.data.mappers

import com.daniel.ping.data.local.room.RecentConversations
import com.daniel.ping.domain.models.RecentConversation
import java.util.Date

object RecentConversationsMapper {
    fun RecentConversation.toRecentConversations(): RecentConversations{
        return RecentConversations(
            senderId = senderId,
            receiverId = receiverId,
            receiverProfileImageUrl = profileImageUrl,
            receiverName = name,
            receiverDescription = description,
            receiverToken = token,
            dateObject = dateObject.toString()
        )
    }
    fun RecentConversations.toRecentConversation(): RecentConversation{
        return RecentConversation(
            senderId = senderId,
            receiverId = receiverId,
            profileImageUrl = receiverProfileImageUrl,
            name = receiverName,
            description = receiverDescription,
            token = receiverToken,
            dateObject = Date(dateObject)
        )
    }
}