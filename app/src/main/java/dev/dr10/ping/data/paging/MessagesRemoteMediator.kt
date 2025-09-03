package dev.dr10.ping.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.dr10.ping.data.local.room.AppDatabase
import dev.dr10.ping.data.local.room.MessageEntity
import dev.dr10.ping.data.mappers.toEntity
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@OptIn(ExperimentalPagingApi::class)
class MessagesRemoteMediator(
    private val supabaseService: SupabaseClient,
    private val database: AppDatabase,
    private val chatId: String
): RemoteMediator<Int, MessageEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        // Determine the `cursor` for pagination based on the load type
        val cursor = when (loadType) {
            LoadType.REFRESH -> null // Initial load, no cursor needed
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                // For appending, get the last message in the local database
                val lastMessage = database.messagesDao().getLastedMessage(chatId)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                // Use the `created_at` of the last message as the cursor
                lastMessage.createdAt
            }
        }

        return try {
            // Fetch messages from the supabase function using the provided parameters
            val messages: List<MessageEntity> = supabaseService.postgrest.rpc(
                function = Constants.RPC_MESSAGES_NAME,
                parameters = buildJsonObject {
                    put(Constants.RPC_MESSAGES_PARAM_CHAT_ID, chatId)
                    cursor?.let { put(Constants.RPC_MESSAGES_PARAM_LAST_TIMESTAMP, it) }
                    put(Constants.RPC_MESSAGES_PARAM_LIMIT_COUNT, state.config.pageSize)
                }
            ).decodeList<MessageData>().map { it.toEntity() }

            // Insert the fetched messages into the local database
            database.withTransaction { database.messagesDao().insertMessagesIfNoExists(messages) }

            MediatorResult.Success(endOfPaginationReached = messages.isEmpty())
        } catch (e: Exception) {
            Log.d(this.javaClass.simpleName, e.message.toString())
            MediatorResult.Error(e)
        }
    }
}