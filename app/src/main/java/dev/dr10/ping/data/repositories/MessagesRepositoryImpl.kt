package dev.dr10.ping.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import dev.dr10.ping.data.local.room.AppDatabase
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.mappers.toMessageData
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.data.models.NewMessageData
import dev.dr10.ping.data.paging.MessagesRemoteMediator
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
     * @param chatId The unique identifier of the chat
     */
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMessages(chatId: String): Flow<PagingData<MessageEntity>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10,
            enablePlaceholders = false
        ),
        remoteMediator = MessagesRemoteMediator(
            supabaseService = supabaseService,
            database = database,
            chatId = chatId
        ),
        pagingSourceFactory = { database.messagesDao().pagingResource(chatId) }
    ).flow

    /**
     * Subscribe to the [Constants.MESSAGES_TABLE] table and listen for new messages
     *
     * @param chatId The unique identifier of the chat
     * @return A [NewMessageData] object containing the new messages and the channel
     */
    @OptIn(SupabaseExperimental::class)
    override suspend fun subscribeAndListenNewMessages(chatId: String): NewMessageData {
        val channel = supabaseService.channel(chatId)
        val insertChangeFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = Constants.MESSAGES_TABLE_SCHEMA) {
            table = Constants.MESSAGES_TABLE
            filter(Constants.RPC_MESSAGES_PARAM_CHAT_ID, FilterOperator.EQ, chatId)
        }
        channel.subscribe()

        val newMessage = insertChangeFlow.map { it.record.toMessageData() }

        return NewMessageData(
            newMessage = newMessage,
            channel = channel
        )
    }

    override suspend fun insertNewMessage(message: MessageEntity) {
        database.messagesDao().insertMessageIfNoExists(message)
    }
}