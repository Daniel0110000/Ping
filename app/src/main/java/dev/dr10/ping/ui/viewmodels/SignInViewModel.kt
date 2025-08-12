package dev.dr10.ping.ui.viewmodels

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.usesCases.AuthWithGoogleUseCase
import dev.dr10.ping.domain.usesCases.FetchAndStoreUserDataUseCase
import dev.dr10.ping.domain.usesCases.SignInWithEmailAndPasswordUseCase
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import dev.dr10.ping.domain.utils.getErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val authWithGoogleUseCase: AuthWithGoogleUseCase,
    private val fetchAndStoreUserDataUseCase: FetchAndStoreUserDataUseCase
): ViewModel() {

    private val _state: MutableStateFlow<SignInState> = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()

    data class SignInState(
        val email: String = "",
        val password: String = "",
        val isSignInSuccessful: Boolean = false,
        val isSignInLoading: Boolean = false,
        val isGoogleSignInLoading: Boolean = false,
        val isIncompleteProfile: Boolean = false,
        @StringRes val errorMessage: Int? = null
    )

    fun onSignIn() {
        viewModelScope.launch {
            updateState { copy(isSignInLoading = true) }

            when (val result = signInWithEmailAndPasswordUseCase(_state.value.email, _state.value.password)) {
                is Result.Success -> processUserData()
                is Result.Error -> updateState { copy(isSignInSuccessful = false, errorMessage = result.error.getErrorMessageId()) }
            }

            updateState { copy(isSignInLoading = false) }
        }
    }

    fun onGoogleSignIn(context: Context) {
        viewModelScope.launch {
            updateState { copy(isGoogleSignInLoading = true) }

            when (val result = authWithGoogleUseCase(context)) {
                is Result.Success -> processUserData()
                is Result.Error -> updateState { copy(isSignInSuccessful = false, errorMessage = result.error.getErrorMessageId()) }
            }

            updateState { copy(isGoogleSignInLoading = false) }
        }
    }

    private suspend fun processUserData() {
        when (val result = fetchAndStoreUserDataUseCase()) {
            is Result.Success -> updateState { copy(isSignInSuccessful = result.data) }
            is Result.Error -> updateState {
                if (result.error == ErrorType.USER_DATA_NOT_FOUND) copy(isIncompleteProfile = true)
                else copy(isSignInSuccessful = false, errorMessage = result.error.getErrorMessageId())
            }
        }
    }

    fun onEmailChanged(email: String) {
        updateState { copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        updateState { copy(password = password) }
    }

    fun clearErrorMessage() {
        updateState { copy(errorMessage = null) }
    }

    private fun updateState(newState: SignInState.() -> SignInState) {
        _state.value = _state.value.newState()
    }

}