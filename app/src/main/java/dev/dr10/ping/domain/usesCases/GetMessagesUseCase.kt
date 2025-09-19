package dev.dr10.ping.domain.usesCases

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.MessageModel
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.MessageUtils
import dev.dr10.ping.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMessagesUseCase(
    private val repository: MessagesRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(receiverId: String): Result<Flow<PagingData<MessageModel>>, ErrorType> = try {
        // Get the sender ID
        val senderId = authRepository.getCurrentUserId()!!
        // Generate the chat ID
        val conversationId = MessageUtils.generateConversationId(senderId, receiverId)
        // Get the messages of the current user and the receiver
        val result = repository.getMessages(conversationId).map { pagingData -> pagingData.map { it.toModel() } }
        Result.Success(result)
    } catch (e: Exception) {
        Log.d(this.javaClass.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}