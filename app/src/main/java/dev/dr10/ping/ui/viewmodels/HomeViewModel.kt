package dev.dr10.ping.ui.viewmodels

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dev.dr10.ping.domain.models.RecentConversationModel
import dev.dr10.ping.domain.usesCases.GetProfileImageUseCase
import dev.dr10.ping.domain.usesCases.GetRecentConversationsUseCase
import dev.dr10.ping.domain.usesCases.InitializeRealtimeRecentConversationsUseCase
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import dev.dr10.ping.ui.extensions.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProfileImageUseCase: GetProfileImageUseCase,
    private val getRecentConversationsUseCase: GetRecentConversationsUseCase,
    private val initializeRealtimeRecentConversationsUseCase: InitializeRealtimeRecentConversationsUseCase
): ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _recentConversations = MutableStateFlow<Flow<PagingData<RecentConversationModel>>>(emptyFlow())
    val recentConversations: StateFlow<Flow<PagingData<RecentConversationModel>>> = _recentConversations

    data class HomeState(
        val profileImage: Bitmap? = null,
        @StringRes val errorMessage: Int? = null
    )

    init {
        viewModelScope.launch {
            getRecentConversationsUseCase()
                .onSuccess { conversations -> _recentConversations.value = conversations }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }

            getProfileImageUseCase()
                .onSuccess { image -> updateState { copy(profileImage = image) } }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }

            initializeRealtimeRecentConversationsUseCase()
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
        }
    }

    fun clearErrorMessage() {
        updateState { copy(errorMessage = null) }
    }

    private fun updateState(update: HomeState.() -> HomeState) {
        _state.value = _state.value.update()
    }

}