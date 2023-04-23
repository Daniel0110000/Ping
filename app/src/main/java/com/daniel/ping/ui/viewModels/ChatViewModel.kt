package com.daniel.ping.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.repositories.ChatRepository
import com.daniel.ping.domain.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel class responsible for handling message-related UI and business logic
 * @property _allMessages MutableLiveData for storing a list of all the chat messages in the conversation
 * @property allMessages LiveData for accessing the list of all chat messages
 * @property _isLoading MutableLiveData to indicate if the upload is in progress
 * @property isLoading LiveData for access progress status
 * @property _isOnLine MutableLiveData to indicate if the user is available
 * @property isOnline Livedata for access user status
 * @property messageText MutableLiveData for storing the message text entered by the user
 * @property receiverUser MutableLiveData for storing the user the current user is chatting with
 * @property userId the ID of the current user
 * @property chatRepository an instance of ChatRepository for handling message-related data operations
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val auth: AuthenticationRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _allMessages = MutableLiveData<ArrayList<Chat>>()
    val allMessages: LiveData<ArrayList<Chat>> = _allMessages

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isOnLine = MutableLiveData(false)
    val isOnline: LiveData<Boolean> = _isOnLine

    private val messageText = MutableLiveData<String>()

    private val receiverUser = MutableLiveData<User>()

    val userId = auth.getString(Constants.KEY_USER_ID)

    init {
        setMessageText("")
        _isLoading.value = true
    }

    /**
     * Sends a message to the user the current user is chatting with
     */
    fun sendMessage() {
        if (messageText.value?.isNotEmpty() == true) {
            viewModelScope.launch(Dispatchers.IO) {
                val message: HashMap<String, Any> = hashMapOf()
                message[Constants.KEY_SENDER_ID] = auth.getString(Constants.KEY_USER_ID)
                message[Constants.KEY_RECEIVER_ID] = receiverUser.value?.id.toString()
                message[Constants.KEY_MESSAGE] = messageText.value.toString()
                message[Constants.KEY_TIMESTAMP] = Date()
                chatRepository.sendMessage(message)
            }
        }
    }

    /**
     * Listens to new messages in the current chat conversation
     */
    fun listenerMessages() {
        viewModelScope.launch {
            chatRepository.listenerMessages(
                auth.getString(Constants.KEY_USER_ID),
                receiverUser.value?.id.toString()
            ) { messages ->
                _allMessages.value = messages
                _isLoading.value = false
            }
        }
    }

     fun availabilityUser(){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.listenerAvailabilityOfReceiver(receiverUser.value?.id.toString()){ online ->
                _isOnLine.postValue(online == 1)
            }
        }
    }

    fun setMessageText(value: String) {
        messageText.value = value
    }

    fun setReceiverUser(value: User) {
        receiverUser.value = value
    }
}