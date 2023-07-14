package com.daniel.ping.ui.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.R
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val application: Application
): ViewModel() {

    private val _state = MutableStateFlow(ProSetupState())
    val state = _state.asStateFlow()

    // Data class that represents the state of the Profile Setup screen.
    data class ProSetupState(
        val profileImage: Uri? = null,
        val name: String = "",
        val description: String = "",
        val isLoading: Boolean = false,
        val message: String = "",
        val completed: Boolean = false
    )

    // Initializes the name and description of the user to empty strings.
    init {
        setNameAndDescription("", "")
    }

    /**
     * Handles the "Finish" button click
     * Inserts the user's profile information into the database
     */
    fun finish(){
        if(!areFieldsValid()){
            setMessage(application.getString(R.string.emptyFieldsErrorMessage))
            return
        }

        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            // val bitmapImage = ImageConverter.uriToBitmap(application, state.value.profileImage!!)
            val dataResult = authenticationRepository.insertProfileDescription(
                hashMapOf(
                    Constants.KEY_NAME to state.value.name,
                    Constants.KEY_DESCRIPTION to state.value.description
                ),
                authenticationRepository.getString(Constants.KEY_USER_ID),
                state.value.profileImage!!
            )
            when(dataResult){
                is Resource.Success -> handleInsertSuccess(dataResult.data!!)
                is Resource.Error -> handleInsertError(dataResult.message.toString())
            }
        }

    }

    /**
     * Handles teh successful insertion of the user's profile information into the database
     * Saves the user's information to shared preferences and sets the "completed" state to tru
     */
    private fun handleInsertSuccess(rtl: Task<Void>){
        rtl.addOnSuccessListener {
            authenticationRepository.putBooleanToPrefs(Constants.KEY_IS_PROFILE_FULLY_COMPLETED, true)
            authenticationRepository.putStringToPrefs(Constants.KEY_NAME, state.value.name)
            authenticationRepository.putStringToPrefs(Constants.KEY_DESCRIPTION, state.value.description)
            setCompleted(true)
            setIsLoading(false)
        }
        rtl.addOnFailureListener { e ->
            setMessage(e.message.toString())
            setIsLoading(false)
        }
    }

    // Handles errors that occur during the insertion of the user's profile information into the database
    private fun handleInsertError(errorMessage: String){
        setMessage(errorMessage)
        setIsLoading(false)
    }

    // Validates the user's input fields
    private fun areFieldsValid(): Boolean {
        return state.value.name.isNotEmpty() && state.value.name.contains(Regex("[a-zA-Z]"))
                && state.value.description.isNotEmpty() && state.value.description.contains(Regex("[a-zA-Z]"))
                && state.value.profileImage != null
    }

    fun setNameAndDescription(name: String, description: String){
        viewModelScope.launch {
            _state.update { it.copy(
                name = name,
                description = description
            ) }
        }
    }

    fun setProfileImage(image: Uri?){
        viewModelScope.launch {
            _state.update { it.copy(
                profileImage = image
            ) }
        }
    }

    fun setMessage(value: String){
        viewModelScope.launch {
            _state.update { it.copy(
                message = value
            ) }
        }
    }

    private fun setIsLoading(value: Boolean){
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = value
            ) }
        }
    }

     fun setCompleted(value: Boolean){
        viewModelScope.launch {
            _state.update { it.copy(
                completed = value
            ) }
        }
    }

}