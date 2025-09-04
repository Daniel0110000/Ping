package dev.dr10.ping.domain.repositories

import androidx.paging.PagingData
import dev.dr10.ping.data.local.room.RecentConversationEntity
import dev.dr10.ping.data.models.RecentConversationData
import kotlinx.coroutines.flow.Flow

interface ConversationsRepository {

    suspend fun upsertRecentConversation(conversationData: RecentConversationData)

    suspend fun getRecentConversations(): Flow<PagingData<RecentConversationEntity>>

    suspend fun insertNewRecentConversation(conversationEntity: RecentConversationEntity)

    suspend fun updateConversation(conversationEntity: RecentConversationEntity)

    suspend fun subscribeToRecentConversations(): Flow<RecentConversationEntity?>
}