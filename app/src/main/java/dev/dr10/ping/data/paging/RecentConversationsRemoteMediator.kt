package dev.dr10.ping.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.dr10.ping.data.local.room.AppDatabase
import dev.dr10.ping.data.local.room.RecentConversationEntity
import dev.dr10.ping.data.models.RecentConversationData
import dev.dr10.ping.domain.extensions.toProfileImageUrl
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.domain.utils.MessageUtils
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

@OptIn(ExperimentalPagingApi::class)
class RecentConversationsRemoteMediator(
    private val usersRepository: UsersRepository,
    private val currentUserId: String,
    private val supabaseService: SupabaseClient,
    private val database: AppDatabase
): RemoteMediator<Int, RecentConversationEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecentConversationEntity>
    ): MediatorResult {
        // Extract the page size from the pager config
        val pageSize = state.config.pageSize
        // Determine the load key
        val loadKey = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                // Extract the last item of the state
                val lastItem = state.lastItemOrNull()
                // If the last item is null, return 0
                if (lastItem == null) 0
                // Otherwise, return the sum of the size of all pages
                else state.pages.sumOf { it.data.size }
            }
        }

        return try {
            // Fetch recent conversations from [Constants.CONVERSATIONS_TABLE]
            val allRecentConversations = supabaseService.from(Constants.CONVERSATIONS_TABLE).select {
                range(loadKey.toLong(), (loadKey + pageSize - 1).toLong())
            }.decodeList<RecentConversationData>()

            // Create a list of [RecentConversationEntity] from the fetched data
            val recentConversations = allRecentConversations.map {
                val userId = MessageUtils.extractUserId(currentUserId, it.sender_id, it.receiver_id)
                val userData = usersRepository.fetchSimpleUserData(userId)

                RecentConversationEntity(
                    conversationId = it.conversation_id,
                    userId = userId,
                    username = userData.username,
                    profilePictureUrl = userData.profile_image.toProfileImageUrl(),
                    updatedAt = it.updated_at
                )
            }

            // Insert the recent conversations into the local database
            database.withTransaction {
                database.recentConversationsDao().insertRecentConversationsIfNotExists(recentConversations)
            }

            MediatorResult.Success(recentConversations.size < pageSize)
        } catch (e: Exception) {
            Log.d(this.javaClass.simpleName, e.message.toString())
            MediatorResult.Error(e)
        }
    }
}