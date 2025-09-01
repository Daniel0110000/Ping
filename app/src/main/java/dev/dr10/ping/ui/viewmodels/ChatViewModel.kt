package dev.dr10.ping.ui.viewmodels

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.usesCases.SendMessageUseCase
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val sendMessageUseCase: SendMessageUseCase
): ViewModel() {

    private val _state: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private var receiverUserData: UserProfileModel? = null

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

    fun setReceiverData(data: UserProfileModel) { receiverUserData = data }

    fun setMessage(value: String) { updateState { copy(message = value) } }

    private fun updateState(update: ChatState.() -> ChatState) {
        _state.value = _state.value.update()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(this.javaClass.simpleName, "ChatViewModel cleared")
    }
}