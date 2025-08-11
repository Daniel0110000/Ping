package dev.dr10.ping.ui.viewmodels

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dr10.ping.domain.usesCases.ProfileSetupUseCase
import dev.dr10.ping.domain.utils.Result
import dev.dr10.ping.domain.utils.getErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileSetupViewModel(
    private val profileSetupUseCase: ProfileSetupUseCase
): ViewModel() {

    private val _state: MutableStateFlow<ProfileSetupState> = MutableStateFlow(ProfileSetupState())
    val state: StateFlow<ProfileSetupState> = _state.asStateFlow()

    data class ProfileSetupState(
        val profileImage: Uri? = null,
        val username: String = "",
        val bio: String = "",
        val isLoading: Boolean = false,
        val isProfileSetupSuccessful: Boolean = false,
        @StringRes val errorMessage: Int? = null
    )

    fun onFinish() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val result = profileSetupUseCase(
                profileImageUri = _state.value.profileImage,
                username = _state.value.username,
                bio = _state.value.bio
            )

            updateState {
                when (result) {
                    is Result.Success -> copy(
                        isProfileSetupSuccessful = result.data,
                        profileImage = null,
                        username = "",
                        bio = "",
                    )
                    is Result.Error -> copy(isProfileSetupSuccessful = false, errorMessage = result.error.getErrorMessageId())
                }
            }
            updateState { copy(isLoading = false) }
        }
    }

    fun onProfileImageChanged(imageUri: Uri?) {
        updateState { copy(profileImage = imageUri) }
    }

    fun onUsernameChanged(username: String) {
        updateState { copy(username = username) }
    }

    fun onBioChanged(bio: String) {
        updateState { copy(bio = bio) }
    }

    fun clearErrorMessage() {
        updateState { copy(errorMessage = null) }
    }

    private fun updateState(newState: ProfileSetupState.() -> ProfileSetupState) {
        _state.value = _state.value.newState()
    }

}