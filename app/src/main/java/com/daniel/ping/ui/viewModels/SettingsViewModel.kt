package com.daniel.ping.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    auth: AuthenticationRepository
): ViewModel() {

    // The ViewModel state flow that contains the user name, user description and profile image
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    /**
     * The data class that holds the settings state
     * @param name The name of the user
     * @param description The description of the user
     * @param profileImage The profile image of the user
     */
    data class SettingsState(
        val name: String = "",
        val description: String = "",
        val profileImage: String = "",
    )

    init {
        setUserDetails(
            nameValue = auth.getString(Constants.KEY_NAME),
            descriptionValue = auth.getString(Constants.KEY_DESCRIPTION),
            profileImageValue = auth.getString(Constants.KEY_PROFILE_IMAGE_PATH)
        )
    }

    private fun setUserDetails(nameValue: String, descriptionValue: String, profileImageValue: String){
        viewModelScope.launch {
            _state.update { it.copy(
                name = nameValue,
                description = descriptionValue,
                profileImage = profileImageValue
            ) }
        }
    }

}