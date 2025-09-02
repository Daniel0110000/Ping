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
        WHERE (sender_id = :senderId AND receiver_id = :receiverId)
        OR (sender_id = :receiverId AND receiver_id = :senderId)
        ORDER BY created_at DESC
        """
    )
    fun pagingResource(
        senderId: String,
        receiverId: String
    ): PagingSource<Int, MessageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessagesIfNoExists(messages: List<MessageEntity>)

    @Query("""
        SELECT * FROM messages 
        WHERE (sender_id = :senderId AND receiver_id = :receiverId)
        OR (sender_id = :receiverId AND receiver_id = :senderId)        
        ORDER BY created_at ASC LIMIT 1
    """)
    suspend fun getLastedMessage(
        senderId: String,
        receiverId: String
    ): MessageEntity?

}