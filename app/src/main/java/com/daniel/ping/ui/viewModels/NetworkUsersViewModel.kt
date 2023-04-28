package com.daniel.ping.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.repositories.UserDataRepository
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkUsersViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val authenticationRepository: AuthenticationRepository
): ViewModel(){

    // MutableStateFlow to hold the state of the NetworkUsersViewModel
    private val _state = MutableStateFlow(NetworkUsersState())
    // Immutable StateFlow for external access to the state of the NetworkUsersViewModel
    val state = _state.asStateFlow()

    /**
     * Data class to represent the state of the NetworkUsersViewModel
     * @param allUsers the list of User objects retrieved from the data repository
     * @param isLoading a Boolean flag indicating whether data is currently being retrieved
     * @param message a String message to be displayed in case of an error
     */
    data class NetworkUsersState(
        val allUsers: ArrayList<User> = arrayListOf(),
        val isLoading: Boolean = false,
        val message: String = ""
    )

    /**
     * Initialization block that retrieves all users from the data repository when the ViewModel is created
     */
    init {
        getAllUsers()
    }

    /**
     * Private function that retrieves all users from the data repository and updates the state of the ViewModel.
     */
    private fun getAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {

            setIsLoading(true)

            when (val users = userDataRepository.getAllUsers(authenticationRepository.getString(Constants.KEY_USER_ID))){
                is Resource.Success -> {
                    users.data?.let { setAllUsers(it) }
                    setIsLoading(false)
                }
                is Resource.Error -> {
                    setMessage(users.message.toString())
                    setIsLoading(false)
                }
            }

        }
    }

    private fun setAllUsers(users: ArrayList<User>){
        viewModelScope.launch {
            _state.update { it.copy(
                allUsers = users
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

    private fun setMessage(value: String){
        viewModelScope.launch {
            _state.update { it.copy(
                message = value
            ) }
        }
    }

}