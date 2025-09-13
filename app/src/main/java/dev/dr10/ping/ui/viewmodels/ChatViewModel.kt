package dev.dr10.ping.ui.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.dr10.ping.domain.models.MessageModel
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.usesCases.GetInitialUserPresence
import dev.dr10.ping.domain.usesCases.GetMessagesUseCase
import dev.dr10.ping.domain.usesCases.InitializeRealtimeUserPresenceUseCase
import dev.dr10.ping.domain.usesCases.SendMessageUseCase
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import dev.dr10.ping.ui.extensions.onSuccess
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getInitialUserPresence: GetInitialUserPresence,
    private val initializeRealtimeUserPresenceUseCase: InitializeRealtimeUserPresenceUseCase
): ViewModel() {

    private val _state: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private var receiverUserData: UserProfileModel? = null
    private var currentPresenceChannel: RealtimeChannel? = null

    private val _messages = MutableStateFlow<Flow<PagingData<MessageModel>>>(emptyFlow())
    val messages: StateFlow<Flow<PagingData<MessageModel>>> = _messages

    data class ChatState(
        val message: String = "",
        val isOnline: Boolean = false,
        val lastConnected: String = "",
        @StringRes val errorMessage: Int? = null
    )

    fun initializeAndListenChatSession(data: UserProfileModel) {
        receiverUserData = data
        fetchInitialUserPresence(data.userId)
        viewModelScope.launch {
            val messagesDeferred = async(Dispatchers.IO) { getMessagesUseCase(data.userId) }
            val presenceDeferred = async(Dispatchers.IO) { initializeRealtimeUserPresenceUseCase(data.userId) }

            messagesDeferred.await()
                .onSuccess { flow -> _messages.value = flow.cachedIn(viewModelScope) }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }

            presenceDeferred.await()
                .onSuccess { presence ->
                    currentPresenceChannel = presence.channel
                    presence.newStatus.collect { model ->
                        updateState {
                            copy(
                                isOnline = model.isOnline,
                                lastConnected = if(model.isOnline) "" else Constants.ACTIVE_RECENTLY_MESSAGE
                            )
                        }
                    }
                }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
        }
    }

    fun fetchInitialUserPresence(userId: String) {
        viewModelScope.launch {
            updateState { copy(isOnline = false, lastConnected = "") }
            getInitialUserPresence(userId)
                .onSuccess { status ->
                    updateState {
                        copy(
                            isOnline = status.isOnline,
                            lastConnected = status.lastConnected
                        )
                    }
                }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
        }
    }


    fun sendMessage() {
        viewModelScope.launch {
            sendMessageUseCase(
                receiverUserData = receiverUserData,
                content = state.value.message,
                isOnline = state.value.isOnline
            )
            updateState { copy(message = "") }
        }
    }

    fun stopListening() {
        viewModelScope.launch {
            currentPresenceChannel?.unsubscribe()
            currentPresenceChannel = null
        }
    }

    fun setMessage(value: String) { updateState { copy(message = value) } }

    fun clearErrorMessage() { updateState { copy(errorMessage = null) } }

    private fun updateState(update: ChatState.() -> ChatState) {
        _state.value = _state.value.update()
    }
}