package dev.dr10.ping.ui.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.usesCases.GetSuggestedUsersUseCase
import dev.dr10.ping.domain.usesCases.SearchUserUseCase
import dev.dr10.ping.domain.utils.Result
import dev.dr10.ping.domain.utils.getErrorMessageId
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
        val userSuggestions: List<UserProfileModel> = emptyList(),
        val isSuggestedUsersLoading: Boolean = false,
        @StringRes val errorMessage: Int? = null
    )

    init {
        viewModelScope.launch {
            updateState { copy(isSuggestedUsersLoading = true) }
            when (val result = getSuggestedUsersUseCase()) {
                is Result.Success -> { updateState { copy(userSuggestions = result.data) } }
                is Result.Error -> { updateState { copy(errorMessage = result.error.getErrorMessageId()) } }
            }
            updateState { copy(isSuggestedUsersLoading = false) }
        }

        viewModelScope.launch {
            _state.map { it.search }
                .debounce(400)
                .distinctUntilChanged()
                .mapLatest { query -> searchUserUseCase(query) }
                .collect { users -> }
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