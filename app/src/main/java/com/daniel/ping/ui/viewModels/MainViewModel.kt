package com.daniel.ping.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.domain.repositories.UserDataRepository
import com.daniel.ping.domain.utilities.Resource
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
): ViewModel() {

    private val _message = MutableStateFlow("")
    val message: Flow<String> = _message


    // Initializes the ViewModel by calling the getToken function to get the Firebase Messaging token
    init {
        getToken()
    }

    /**
     * Calls FirebaseMessaging to retrieve the token and passes it to the updateToken function
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
                is Resource.Success -> messaging.data?.addOnFailureListener { e -> _message.value = e.message.toString() }
                is Resource.Error -> _message.value = messaging.message.toString()
            }
        }
    }

}