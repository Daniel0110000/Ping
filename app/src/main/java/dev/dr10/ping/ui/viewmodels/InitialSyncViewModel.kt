package dev.dr10.ping.ui.viewmodels

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.usesCases.GetProfileImageUseCase
import dev.dr10.ping.domain.usesCases.SyncInitialMessagesAndConversationsUseCase
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import dev.dr10.ping.ui.extensions.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InitialSyncViewModel(
    private val syncInitialMessagesAndConversationsUseCase: SyncInitialMessagesAndConversationsUseCase,
    private val getProfileImageUseCase: GetProfileImageUseCase
): ViewModel() {

    private val _state: MutableStateFlow<InitialSyncState> = MutableStateFlow(InitialSyncState())
    val state: StateFlow<InitialSyncState> = _state.asStateFlow()

    data class InitialSyncState(
        val isFinished: Boolean = false,
        val profileImage: Bitmap? = null,
        @StringRes val errorMessage: Int? = null
    )

    init {
        viewModelScope.launch {
            getProfileImageUseCase()
                .onSuccess { image -> updateState { copy(profileImage = image) } }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }

            syncInitialMessagesAndConversationsUseCase()
                .onSuccess { updateState { copy(isFinished = it) } }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
        }
    }

    fun clearErrorMessage() {
        updateState { copy(errorMessage = null) }
    }


    private fun updateState(update: InitialSyncState.() -> InitialSyncState) {
        _state.value = _state.value.update()
    }

}