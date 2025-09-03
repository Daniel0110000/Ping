package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.data.mappers.toEntity
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.MessageUtils
import dev.dr10.ping.domain.utils.Result
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitializeRealtimeChatUseCase(
    private val messagesRepository: MessagesRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(receiverId: String): Result<RealtimeChannel, ErrorType> = try {
        // Get the sender ID from the auth repository
        val senderId = authRepository.getProfileData()!!.userId
        // Generate the chat ID
        val chatId = MessageUtils.generateChatId(senderId, receiverId)

        // Subscribe to the new messages channel
        val newMessageResponse = messagesRepository.subscribeAndListenNewMessages(chatId)

        // Insert new messages into the local database for display in the UI
        CoroutineScope(Dispatchers.IO).launch {
            newMessageResponse.newMessage.collect { message -> messagesRepository.insertNewMessage(message.toEntity()) }
        }

        Result.Success(newMessageResponse.channel)
    } catch (e: Exception) {
        Log.e(this::class.java.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }
}