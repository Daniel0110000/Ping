package dev.dr10.ping.domain.repositories

import androidx.paging.PagingData
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.models.MessageData
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    suspend fun sendMessage(message: MessageData)

    fun getMessages(senderId: String, receiverId: String): Flow<PagingData<MessageEntity>>

}