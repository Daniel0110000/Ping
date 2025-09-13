package dev.dr10.ping.domain.repositories

import androidx.paging.PagingData
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.models.MessageData
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    suspend fun sendMessage(message: MessageData)

    suspend fun getMessages(conversationId: String): Flow<PagingData<MessageEntity>>

    suspend fun subscribeAndListenNewMessages(): Flow<MessageData>

    suspend fun insertNewMessage(message: MessageEntity)

}