package com.daniel.ping.ui.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.ping.data.models.NotificationData
import com.daniel.ping.data.models.PushNotification
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.repositories.ChatRepository
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.handleResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
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

    private val _isLoadingUploadFile = MutableLiveData<Boolean>()
    val isLoadingUploadFile: LiveData<Boolean> = _isLoadingUploadFile

    private val _isOnLine = MutableLiveData(false)
    val isOnline: LiveData<Boolean> = _isOnLine

    private val messageImage = MutableLiveData<Uri?>()

    private val messageFile = MutableLiveData<Uri?>()

    private val fileName = MutableLiveData<String>()

    private val fileSize = MutableLiveData<String>()

    private val messageText = MutableLiveData<String>()

    private val receiverUser = MutableLiveData<User>()

    val userId = auth.getString(Constants.KEY_USER_ID)

    var hasCheckedForConversation = false
    private var conversationId: String = ""


    init {
        setMessageText("")
        _isLoading.value = true
    }

    /**
     * Sends a message to the user the current user is chatting with
     */
    fun sendMessage() {
        if (messageText.value?.isNotEmpty() == true || messageImage.value != null || messageFile.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                setLoadingUploadFile(true)
                chatRepository.sendMessage(
                    createMessage(),
                    messageImage.value,
                    messageFile.value,
                    if(messageFile.value != null) hashMapOf(
                        Constants.KEY_FILE_NAME to fileName.value.toString(),
                        Constants.KEY_FILE_SIZE to fileSize.value.toString()
                    ) else hashMapOf()
                )

                setLoadingUploadFile(false)

                // If the user is not online, a notification is sent
                if(!isOnline.value!!) sendNotification(auth.getString(Constants.KEY_NAME), messageText.value.toString())

                // Update the conversation history
                if(conversationId.isNotEmpty())
                    chatRepository.updateConversation(conversationId)
                else{
                    // If there is no conversation history, ada a new conversation
                    val conversation: HashMap<String, Any> = hashMapOf()
                    conversation[Constants.KEY_SENDER_ID] = auth.getString(Constants.KEY_USER_ID)
                    conversation[Constants.KEY_SENDER_NAME] = auth.getString(Constants.KEY_NAME)
                    conversation[Constants.KEY_SENDER_IMAGE] = auth.getString(Constants.KEY_PROFILE_IMAGE_URL)
                    conversation[Constants.KEY_SENDER_DESCRIPTION] = auth.getString(Constants.KEY_DESCRIPTION)
                    conversation[Constants.KEY_SENDER_FCM_TOKEN] = auth.getString(Constants.KEY_FCM_TOKEN)
                    conversation[Constants.KEY_RECEIVER_ID] = receiverUser.value?.id.toString()
                    conversation[Constants.KEY_RECEIVER_NAME] = receiverUser.value?.name.toString()
                    conversation[Constants.KEY_RECEIVER_IMAGE] = receiverUser.value?.profileImageUrl.toString()
                    conversation[Constants.KEY_RECEIVER_DESCRIPTION] = receiverUser.value?.description.toString()
                    conversation[Constants.KEY_RECEIVER_FCM_TOKEN] = receiverUser.value?.token.toString()
                    conversation[Constants.KEY_TIMESTAMP] = Date()
                    addConversation(conversation)
                }
                clearFields()
            }
        }
    }

    /**
     * Create a HashMap object with the necessary fields for sending a message to the FireStore database
     * @return A HashMap object with the following fields: "senderId", "receiverId", "message", "timestamp", and "dateTime"
     */
    private fun createMessage(): HashMap<String, Any> {
        val senderId = auth.getString(Constants.KEY_USER_ID)
        val receiverId = receiverUser.value?.id.toString()
        return hashMapOf(
            Constants.KEY_SENDER_ID to senderId,
            Constants.KEY_RECEIVER_ID to receiverId,
            Constants.KEY_MESSAGE to messageText.value.toString(),
            Constants.KEY_TIMESTAMP to FieldValue.serverTimestamp(),
            Constants.KEY_DATE_TIME to Date()
        )
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

    /**
     * Sets up a listener to check the availability of the receiver user
     * If the receiver user goes online, it sets the value of _isOnLine to true
     * If the receiver user goes offline, it sets the value of _isOnLine to false
     */
     fun availabilityUser(){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.listenerAvailabilityOfReceiver(receiverUser.value?.id.toString()){ online ->
                _isOnLine.postValue(online == 1)
            }
        }
    }

    /**
     * Suspended function that sends a push notification to the receiver user, notifying them of a new message
     * @param title The title of the notification
     * @param body The body text of the notification
     */
    private suspend fun sendNotification(title: String, body: String){
        val notification = PushNotification(NotificationData(title, body), receiverUser.value?.token.toString())
        chatRepository.sendNotification(notification)
    }

    /**
     * Checks if a conversation already exists between the current user and the receiver user
     * by calling the checkForConversationRemotely function twice with both user IDs
     * Updates the boolean flag hasCheckedForConversation once the check is complete
     */
    fun checkForConversation(){
        val senderId = auth.getString(Constants.KEY_USER_ID)
        val receiverId = receiverUser.value?.id.toString()
        checkForConversationRemotely(senderId, receiverId)
        checkForConversationRemotely(receiverId, senderId)
        hasCheckedForConversation = true
    }

    /**
     * Checks for a conversation between two users using the chatRepository
     * If a conversation already exists, sets the conversationId to the existing conversationId
     * @param senderId The id of the sender user
     * @param receiverId The id of the receiver user
     */
    private fun checkForConversationRemotely(senderId: String, receiverId: String){
        viewModelScope.launch(Dispatchers.IO) {
            val onCompleteListener = chatRepository.checkForConversation(senderId, receiverId)
            onCompleteListener.handleResult(
                onSuccess = { query -> query.addOnCompleteListener(conversationOnCompleteListener) },
                onError = { e -> Log.d(" [ ChatViewModel ] ", "${e.message}") }
            )
        }
    }

    /**
     * Listener to handle the result if querying the FireStore for an existing conversation between two users
     * If a conversation is found, the conversationId is set to the ID of the document
     */
    private val conversationOnCompleteListener = OnCompleteListener<QuerySnapshot>{ task ->
        if(task.isSuccessful && task.result != null && task.result.documents.size > 0){
            val documentSnapshot: DocumentSnapshot = task.result.documents[0]
            conversationId = documentSnapshot.id
        }
    }

    /**
     * Adds a new conversation to the database using the chatRepository
     * @param conversation HashMap containing the conversation data to be added
     */
    private fun addConversation(conversation: HashMap<String, Any>){
        viewModelScope.launch(Dispatchers.IO) {
            val idResult = chatRepository.addConversations(conversation)
            idResult.handleResult(
                onSuccess = { result -> conversationId = result },
                onError = { e -> Log.d(" [ ChatViewModel ] ", e.message.toString()) }
            )
        }
    }

    private fun clearFields(){
        messageImage.postValue(null)
        messageFile.postValue(null)
        fileName.postValue("")
        fileSize.postValue("")
        messageText.postValue("")
    }

    fun setMessageImage(value: Uri?) {
        messageImage.value = value
    }

    fun setMessageFile(value: Uri?){
        messageFile.value = value
    }

    fun setFileDetails(fileNameValue: String, fileSizeValue: String){
        fileName.value = fileNameValue
        fileSize.value = fileSizeValue
    }

    private fun setLoadingUploadFile(isLoading: Boolean){
        if(messageImage.value != null || messageFile.value != null)
            _isLoadingUploadFile.postValue(isLoading)
    }

    fun setMessageText(value: String) {
        messageText.value = value
    }

    fun setReceiverUser(value: User) {
        receiverUser.value = value
    }
}