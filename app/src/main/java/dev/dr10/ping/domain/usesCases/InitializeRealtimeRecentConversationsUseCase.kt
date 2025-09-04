package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.ConversationsRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class InitializeRealtimeRecentConversationsUseCase(
    private val conversationsRepository: ConversationsRepository
) {

    suspend operator fun invoke(): Result<Boolean, ErrorType> = try {
        // Subscribe to recent conversations channel and collect the data
        conversationsRepository.subscribeToRecentConversations().collect { data ->
            data?.let {
                // If the user is not logged in, insert a new recent conversation
                if (it.userId.isNotEmpty()) conversationsRepository.insertNewRecentConversation(it)
                // Otherwise, update the existing recent conversation
                else conversationsRepository.updateConversation(it)
            }
        }
        Result.Success(true)
    } catch (e: Exception) {
        Log.e(this.javaClass.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }
}