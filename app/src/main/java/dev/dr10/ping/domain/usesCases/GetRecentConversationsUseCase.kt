package dev.dr10.ping.domain.usesCases

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.RecentConversationModel
import dev.dr10.ping.domain.repositories.ConversationsRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentConversationsUseCase(
    private val repository: ConversationsRepository
) {

    suspend operator fun invoke(): Result<Flow<PagingData<RecentConversationModel>>, ErrorType> = try {
        // Get the recent conversations of the current user
        val conversations = repository.getRecentConversations().map { pagingData -> pagingData.map { it.toModel() } }
        // Return the result with the recent conversations
        Result.Success(conversations)
    } catch (e: Exception) {
        Log.d(this.javaClass.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}