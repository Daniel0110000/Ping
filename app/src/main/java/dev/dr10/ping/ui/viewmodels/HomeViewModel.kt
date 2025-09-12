package dev.dr10.ping.ui.viewmodels

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dev.dr10.ping.domain.models.RecentConversationModel
import dev.dr10.ping.domain.usesCases.GetProfileImageUseCase
import dev.dr10.ping.domain.usesCases.GetRecentConversationsUseCase
import dev.dr10.ping.domain.usesCases.InitializeRealtimeRecentConversationsUseCase
import dev.dr10.ping.domain.usesCases.UpdateLastConnectedUseCase
import dev.dr10.ping.domain.usesCases.UpdateStatusUseCase
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import dev.dr10.ping.ui.extensions.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProfileImageUseCase: GetProfileImageUseCase,
    private val getRecentConversationsUseCase: GetRecentConversationsUseCase,
    private val initializeRealtimeRecentConversationsUseCase: InitializeRealtimeRecentConversationsUseCase,
    private val updateStatusUseCase: UpdateStatusUseCase,
    private val updateLastConnectedUseCase: UpdateLastConnectedUseCase
): ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _recentConversations = MutableStateFlow<Flow<PagingData<RecentConversationModel>>>(emptyFlow())
    val recentConversations: StateFlow<Flow<PagingData<RecentConversationModel>>> = _recentConversations

    private var presenceJob: Job? = null

    data class HomeState(
        val profileImage: Bitmap? = null,
        @StringRes val errorMessage: Int? = null
    )

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())
        viewModelScope.launch {
            val recentConversationsDeferred = async(Dispatchers.IO) { getRecentConversationsUseCase() }
            val profileImageDeferred = async(Dispatchers.IO) { getProfileImageUseCase() }
            val realtimeConversationsDeferred = async(Dispatchers.IO) { initializeRealtimeRecentConversationsUseCase() }

            recentConversationsDeferred.await()
                .onSuccess { conversations -> _recentConversations.value = conversations }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }

            profileImageDeferred.await()
                .onSuccess { image -> updateState { copy(profileImage = image) } }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }

            realtimeConversationsDeferred.await()
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
        }
    }

    fun clearErrorMessage() {
        updateState { copy(errorMessage = null) }
    }

    private fun updateState(update: HomeState.() -> HomeState) {
        _state.value = _state.value.update()
    }

    private inner class AppLifecycleObserver: DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            updateStatusUseCase(true)
            presenceJob = viewModelScope.launch(Dispatchers.IO) {
                while (isActive) {
                    updateLastConnectedUseCase()
                    delay(30_000)
                }
            }
        }

        override fun onStop(owner: LifecycleOwner) {
            updateStatusUseCase(false)
            presenceJob?.cancel()
        }
    }

}