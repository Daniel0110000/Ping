package dev.dr10.ping.ui.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.usesCases.SignUpWithEmailAndPasswordUseCase
import dev.dr10.ping.domain.utils.Result
import dev.dr10.ping.domain.utils.getErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpWithEmailAndPasswordUseCase: SignUpWithEmailAndPasswordUseCase
): ViewModel() {

    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    data class SignUpState(
        val email: String = "",
        val password: String = "",
        val isSignUpSuccessful: Boolean = false,
        val isSignUpLoading: Boolean = false,
        val isGoogleSignInLoading: Boolean = false,
        @StringRes val errorMessage: Int? = null
    )

    fun onSignUp() {
        viewModelScope.launch {
            updateState { copy(isSignUpLoading = true) }
            when (val result = signUpWithEmailAndPasswordUseCase(state.value.email, state.value.password)) {
                is Result.Success -> { updateState { copy(isSignUpSuccessful = result.data) } }
                is Result.Error -> { updateState { copy(isSignUpSuccessful = false, errorMessage = result.error.getErrorMessageId()) } }
            }
            updateState { copy(isSignUpLoading = false) }
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

    private fun updateState(newState: SignUpState.() -> SignUpState) {
        _state.value = _state.value.newState()
    }

}