package dev.dr10.ping.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import dev.dr10.ping.data.local.room.AppDatabase
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.data.paging.MessagesRemoteMediator
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class MessagesRepositoryImpl(
    private val supabaseService: SupabaseClient,
    private val database: AppDatabase
    ): MessagesRepository {

    /**
     * Save a message to the [Constants.MESSAGES_TABLE] table using the provided message data
     *
     * @param message The message data to be saved
     */
    override suspend fun sendMessage(message: MessageData) {
        supabaseService.from(Constants.MESSAGES_TABLE).insert(message)
    }

    /**
     * Get all messages of the current user and the receiver using [Pager] and [RemoteMediator]
     *
     * @param senderId The ID of the current user
     * @param receiverId The ID of the receiver user
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getMessages(
        senderId: String,
        receiverId: String
    ): Flow<PagingData<MessageEntity>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10,
            enablePlaceholders = false
        ),
        remoteMediator = MessagesRemoteMediator(
            supabaseService = supabaseService,
            database = database,
            senderId = senderId,
            receiverId = receiverId
        ),
        pagingSourceFactory = { database.messagesDao().pagingResource(senderId, receiverId) }
    ).flow


}