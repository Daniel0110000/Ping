package dev.dr10.ping.domain.repositories

import androidx.paging.PagingData
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.models.MessageData
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    suspend fun sendMessage(message: MessageData)

    suspend fun getMessages(conversationId: String): Flow<PagingData<MessageEntity>>

    suspend fun fetchAllMessages(userId: String): List<MessageData>

    suspend fun fetchNewMessages(userId: String, lastMessageId: Int): List<MessageData>

    suspend fun saveAllMessages(messages: List<MessageData>)

    suspend fun subscribeAndListenNewMessages(): Flow<MessageData>

    suspend fun insertNewMessage(message: MessageEntity)

    suspend fun getLastMessageId(): Int?
}