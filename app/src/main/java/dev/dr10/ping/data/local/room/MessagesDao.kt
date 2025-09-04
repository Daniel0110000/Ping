package dev.dr10.ping.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessagesDao {

    @Query(
        """
        SELECT * FROM messages
        WHERE conversation_id = :conversationId
        ORDER BY created_at DESC
        """
    )
    fun pagingResource(conversationId: String): PagingSource<Int, MessageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessagesIfNoExists(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessageIfNoExists(message: MessageEntity)

    @Query("""
        SELECT * FROM messages 
        WHERE conversation_id = :conversationId
        ORDER BY created_at ASC LIMIT 1
    """)
    suspend fun getLastedMessage(conversationId: String): MessageEntity?

}