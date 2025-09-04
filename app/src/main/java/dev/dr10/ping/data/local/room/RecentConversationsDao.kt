package dev.dr10.ping.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecentConversationsDao {

    @Query("SELECT * FROM recent_conversations ORDER BY updated_at DESC")
    fun pagingResource(): PagingSource<Int, RecentConversationEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentConversationsIfNotExists(conversations: List<RecentConversationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentConversation(conversation: RecentConversationEntity)

    @Query("UPDATE recent_conversations SET updated_at = :newUpdatedAt WHERE conversation_id = :conversationId")
    suspend fun updateUpdateAtField(conversationId: String, newUpdatedAt: String)

}