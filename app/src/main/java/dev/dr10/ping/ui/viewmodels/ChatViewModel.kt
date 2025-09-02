package dev.dr10.ping.ui.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.dr10.ping.domain.models.MessageModel
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.usesCases.GetMessagesUseCase
import dev.dr10.ping.domain.usesCases.SendMessageUseCase
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import dev.dr10.ping.ui.extensions.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
): ViewModel() {

    private val _state: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private var receiverUserData: UserProfileModel? = null

    private val _messages = MutableStateFlow<Flow<PagingData<MessageModel>>>(emptyFlow())
    val messages: StateFlow<Flow<PagingData<MessageModel>>> = _messages

    data class ChatState(
        val message: String = "",
        @StringRes val errorMessage: Int? = null
    )

    fun sendMessage() {
        viewModelScope.launch {
            sendMessageUseCase(
                receiverId = receiverUserData?.userId ?: "",
                content = state.value.message
            ).onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
            updateState { copy(message = "") }
        }
    }

    fun loadMessagesForReceiver(data: UserProfileModel) {
        receiverUserData = data
        viewModelScope.launch {
            getMessagesUseCase(data.userId)
                .onSuccess { flow -> _messages.value = flow.cachedIn(viewModelScope) }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
        }
    }

    fun setMessage(value: String) { updateState { copy(message = value) } }

    fun clearErrorMessage() { updateState { copy(errorMessage = null) } }

    private fun updateState(update: ChatState.() -> ChatState) {
        _state.value = _state.value.update()
    }
}