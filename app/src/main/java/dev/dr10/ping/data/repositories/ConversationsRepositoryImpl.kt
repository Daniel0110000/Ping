package dev.dr10.ping.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.dr10.ping.data.local.room.AppDatabase
import dev.dr10.ping.data.local.room.RecentConversationEntity
import dev.dr10.ping.data.mappers.toJsonObject
import dev.dr10.ping.data.mappers.toRecentConversationData
import dev.dr10.ping.data.models.RecentConversationData
import dev.dr10.ping.data.paging.RecentConversationsRemoteMediator
import dev.dr10.ping.domain.extensions.toProfileImageUrl
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.ConversationsRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.domain.utils.MessageUtils
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class ConversationsRepositoryImpl(
    private val supabaseService: SupabaseClient,
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
    private val database: AppDatabase
): ConversationsRepository {

    /**
     * Upsert recent conversation to supabase
     *
     * @param conversationData Recent conversation data to be upserted
     */
    override suspend fun upsertRecentConversation(conversationData: RecentConversationData) {
        supabaseService.postgrest.rpc(
            function = Constants.RPC_UPSERT_CONVERSATION_NAME,
            parameters = conversationData.toJsonObject()
        )
    }

    /**
     * Get all recent conversations of the current user using [Pager] and [androidx.paging.RemoteMediator]
     *
     * @return All recent conversations as [PagingData]
     */
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getRecentConversations(): Flow<PagingData<RecentConversationEntity>> {
        val currentUserId = authRepository.getProfileData()!!.userId
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                prefetchDistance = 8,
                enablePlaceholders = false
            ),
            remoteMediator = RecentConversationsRemoteMediator(
                usersRepository = usersRepository,
                supabaseService = supabaseService,
                database = database,
                currentUserId = currentUserId

            ),
            pagingSourceFactory = { database.recentConversationsDao().pagingResource() }
        ).flow
    }

    /**
     * Insert new recent conversation to local database
     *
     * @param conversationEntity New recent conversation to be inserted
     */
    override suspend fun insertNewRecentConversation(conversationEntity: RecentConversationEntity) {
        database.recentConversationsDao().insertRecentConversation(conversationEntity)
    }

    /**
     * Update `update_at` field of a recent conversation using the `conversationId`
     *
     * @param conversationEntity The data to update the field
     */
    override suspend fun updateConversation(conversationEntity: RecentConversationEntity) {
        database.recentConversationsDao().updateUpdateAtField(
            conversationId = conversationEntity.conversationId,
            newUpdatedAt = conversationEntity.updatedAt
        )
    }

    /**
     * Subscribe to the [Constants.CONVERSATIONS_TABLE] table and listen for new or update conversations
     *
     * @return A Flow with the new or update conversations
     */
    override suspend fun subscribeToRecentConversations(): Flow<RecentConversationEntity?> {
        val currentUserId = authRepository.getProfileData()!!.userId
        val channel = supabaseService.channel("${Constants.CONVERSATIONS_TABLE}-$currentUserId")
        val recentConversationChangeFlow = channel.postgresChangeFlow<PostgresAction>(
            schema = Constants.TABLE_SCHEMA
        ) { table = Constants.CONVERSATIONS_TABLE }
        channel.subscribe()

        return recentConversationChangeFlow.mapNotNull {action ->
            when (action) {
                is PostgresAction.Insert -> handleInsertConversation(currentUserId, action)
                is PostgresAction.Update -> handleUpdateConversation(action)
                else -> null
            }
        }
    }

    /**
     * Handle the insertion of a new conversation
     *
     * @param currentUserId The current user's ID
     * @param action The postgres action to handle
     * @return The new recent conversation entity
     */
    private suspend fun handleInsertConversation(
        currentUserId: String,
        action: PostgresAction.Insert
    ): RecentConversationEntity {
        val newConversation = action.record.toRecentConversationData()
        val userId = MessageUtils.extractUserId(currentUserId, newConversation.sender_id, newConversation.receiver_id)
        val userData = usersRepository.fetchSimpleUserData(userId)

        return RecentConversationEntity(
            conversationId = newConversation.conversation_id,
            userId = userId,
            username = userData.username,
            profilePictureUrl = userData.profile_image.toProfileImageUrl(),
            updatedAt = newConversation.updated_at
        )
    }

    /**
     * Handle the update of a conversation
     *
     * @param action The postgres action to handle
     * @return The updated recent conversation entity
     */
    private fun handleUpdateConversation(action: PostgresAction.Update): RecentConversationEntity {
        val updateConversationTo = action.record.toRecentConversationData()
        return RecentConversationEntity(
            conversationId = updateConversationTo.conversation_id,
            userId = "",
            username = "",
            profilePictureUrl = "",
            updatedAt = updateConversationTo.updated_at
        )
    }

}