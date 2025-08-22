package dev.dr10.ping.ui.viewmodels

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.usesCases.GetProfileImageUseCase
import dev.dr10.ping.domain.utils.getErrorMessageId
import dev.dr10.ping.ui.extensions.onError
import dev.dr10.ping.ui.extensions.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProfileImageUseCase: GetProfileImageUseCase
): ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    data class HomeState(
        val profileImage: Bitmap? = null,
        @StringRes val errorMessage: Int? = null
    )

    init {
        viewModelScope.launch {
            getProfileImageUseCase()
                .onSuccess { image -> updateState { copy(profileImage = image) } }
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