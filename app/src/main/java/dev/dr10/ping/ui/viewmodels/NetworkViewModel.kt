package dev.dr10.ping.ui.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.usesCases.GetSuggestedUsersUseCase
import dev.dr10.ping.domain.usesCases.SearchUserUseCase
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import dev.dr10.ping.ui.extensions.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NetworkViewModel(
    private val getSuggestedUsersUseCase: GetSuggestedUsersUseCase,
    private val searchUserUseCase: SearchUserUseCase
): ViewModel() {

    private val _state: MutableStateFlow<NetworkState> = MutableStateFlow(NetworkState())
    val state: StateFlow<NetworkState> = _state.asStateFlow()

    data class NetworkState(
        val search: String = "",
        val users: List<UserProfileModel>? = null,
        val userSuggestions: List<UserProfileModel> = emptyList(),
        val isSearchUserLoading: Boolean = false,
        val isSuggestedUsersLoading: Boolean = false,
        @StringRes val errorMessage: Int? = null
    )

    init {
        viewModelScope.launch {
            updateState { copy(isSuggestedUsersLoading = true) }

            getSuggestedUsersUseCase()
                .onSuccess { users -> updateState { copy(userSuggestions = users) }  }
                .onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }

            updateState { copy(isSuggestedUsersLoading = false) }
        }

        viewModelScope.launch {
            _state.map { it.search }
                .debounce(300)
                .distinctUntilChanged()
                .mapLatest { query ->
                    if (query.isNotBlank()) {
                        updateState { copy(isSuggestedUsersLoading = true) }
                        val result = searchUserUseCase(query)
                        updateState { copy(isSuggestedUsersLoading = false) }
                        result
                    }
                    else null
                }
                .collect { result ->
                    result
                        ?.onSuccess { users -> updateState { copy(users = users) } }
                        ?.onError { err -> updateState { copy(errorMessage = err.getErrorMessageId()) } }
                        ?: updateState { copy(users = null) }
                }
        }
    }

    fun setSearchText(value: String) { updateState { copy(search = value) } }

    fun clearErrorMessage() {
        updateState { copy(errorMessage = null) }
    }

    private fun updateState(update: NetworkState.() -> NetworkState) {
        _state.value = _state.value.update()
    }

}