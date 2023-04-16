package com.daniel.ping.ui.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.repositories.UserDataRepository
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.ImageConverter
import com.daniel.ping.domain.utilities.Resource
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    authenticationRepository: AuthenticationRepository
): ViewModel() {

    // The ViewModel state flow that represents the main state of the application
    // Contains the user's name, profile image, and message
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    /**
     * The data class that holds the main state of the application
     * @param name the name of the user
     * @param profileImage the profile image of the user
     * @param message the message to display in the application
     */
    data class MainState(
        val name: String = "",
        val profileImage: Bitmap? = null,
        val message: String = ""
    )

    // Initializes the ViewModel state by calling getToken() and setting the user's name and profile image
    init {
        getToken()
        setName(authenticationRepository.getString(Constants.KEY_NAME))
        setProfileImage(ImageConverter.decodeFromString(authenticationRepository.getString(Constants.KEY_IMAGE)))
    }

    /**
     * Initializes the ViewModel state by calling getToken() and setting the user's name and profile image
     */
    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener(this::updateToken)
    }

    /**
     * Updates the token in the user data repository via a coroutine in the IO dispatcher
     * The _message MutableStateFlow is updated based on the success or failure of the update
     * @param token the token to update
     */
    private fun updateToken(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            when (val messaging = userDataRepository.updateToken(token)){
                is Resource.Success -> messaging.data?.addOnFailureListener { e -> setMessage(e.message.toString()) }
                is Resource.Error -> setMessage(messaging.message.toString())
            }
        }
    }

    private fun setName(value: String){
        viewModelScope.launch {
            _state.update { it.copy(
                name = value
            ) }
        }
    }

    private fun setProfileImage(value: Bitmap){
        viewModelScope.launch {
            _state.update { it.copy(
                profileImage = value
            ) }
        }
    }

    private fun setMessage(value: String){
        viewModelScope.launch {
            _state.update { it.copy(
                message = value
            ) }
        }
    }

}