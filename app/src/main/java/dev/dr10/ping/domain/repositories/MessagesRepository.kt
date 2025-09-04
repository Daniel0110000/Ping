package dev.dr10.ping.domain.repositories

import androidx.paging.PagingData
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.data.models.NewMessageData
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    suspend fun sendMessage(message: MessageData)

    suspend fun getMessages(conversationId: String): Flow<PagingData<MessageEntity>>

    suspend fun subscribeAndListenNewMessages(conversationId: String): NewMessageData

    suspend fun insertNewMessage(message: MessageEntity)

}