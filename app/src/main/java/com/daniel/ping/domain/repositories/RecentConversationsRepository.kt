package com.daniel.ping.domain.repositories

import com.daniel.ping.domain.models.RecentConversation
import com.daniel.ping.domain.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface RecentConversationsRepository {
    suspend fun listerRecentConversations(senderId: String): Flow<List<RecentConversation>>
}