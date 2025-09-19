package dev.dr10.ping.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.dr10.ping.data.local.room.AppDatabase
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.mappers.toEntity
import dev.dr10.ping.data.mappers.toMessageData
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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
     * @param conversationId The unique identifier of the chat
     */
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMessages(conversationId: String): Flow<PagingData<MessageEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { database.messagesDao().pagingResource(conversationId) }
        ).flow

    /**
     * Fetch all messages of the current user
     *
     * @param userId The current user's ID
     * @return All messages of the current user
     */
    override suspend fun fetchAllMessages(userId: String): List<MessageData> =
        supabaseService.postgrest.rpc(
            function = Constants.RPC_FETCH_ALL_MESSAGES_NAME,
            parameters = buildJsonObject { put(Constants.RPC_USER_ID_PARAM, userId) }
        ).decodeList<MessageData>()

    /**
     * Fetch new messages of the current user
     *
     * @param userId The current user's ID
     * @param lastMessageId The last message ID saved in local database
     * @return New messages of the current user
     */
    override suspend fun fetchNewMessages(
        userId: String,
        lastMessageId: Int
    ): List<MessageData> = supabaseService.postgrest.rpc(
            function = Constants.RPC_FETCH_NEW_MESSAGES_NAME,
            parameters = buildJsonObject {
                put(Constants.RPC_USER_ID_PARAM, userId)
                put(Constants.RPC_LAST_MESSAGE_ID_PARAM, lastMessageId)
            }
        ).decodeList<MessageData>()

    /**
     * Save all messages to local database
     *
     * @param messages All messages to be saved
     */
    override suspend fun saveAllMessages(messages: List<MessageData>) {
        database.withTransaction {
            database.messagesDao().insertMessagesIfNoExists(messages.map { it.toEntity() })
        }
    }

    /**
     * Subscribe to the [Constants.MESSAGES_TABLE] table and listen for new messages
     *
     * @return A [Flow] of [MessageData] objects representing the new messages
     */
    @OptIn(SupabaseExperimental::class)
    override suspend fun subscribeAndListenNewMessages(): Flow<MessageData> {
        val channel = supabaseService.channel(Constants.NEW_MESSAGES_CHANNEL_NAME)
        val newMessagesFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = Constants.TABLE_SCHEMA) { table = Constants.MESSAGES_TABLE }
        channel.subscribe()

        return newMessagesFlow.map { it.record.toMessageData() }
    }

    /**
     * Insert a new message into the [Constants.MESSAGES_TABLE] table
     *
     * @param message The message data to be inserted into the table
     */
    override suspend fun insertNewMessage(message: MessageEntity) {
        database.messagesDao().insertMessageIfNoExists(message)
    }

    override suspend fun getLastMessageId(): Int? = database.messagesDao().getLastMessageId()
}