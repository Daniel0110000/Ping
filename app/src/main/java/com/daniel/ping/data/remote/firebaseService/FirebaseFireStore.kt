package com.daniel.ping.data.remote.firebaseService

import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.RecentConversation
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * A suspend function to check if a user with the given email is already registered in FireStore
 * @param email the email to check if already registered
 * @return a Boolean indicating whether the user is already registered or not
 */
suspend fun FirebaseFirestore.userAlreadyRegistered(email: String): Boolean =
    suspendCoroutine { count ->
        collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL, email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if(querySnapshot != null && !querySnapshot.isEmpty) count.resume(true)
                else count.resume(false)
            }
            .addOnFailureListener { count.resume(false) }
    }

/**
 * Inserts the email into the user's document in the database
 * @param email The email HashMao to insert
 * @return A Task that be used to monitor the status of the operation
 */
fun FirebaseFirestore.insertEmail(email: HashMap<String, Any>): Task<DocumentReference> =
    collection(Constants.KEY_COLLECTION_USERS).add(email)

/**
 * Updates the user's profile description in the database
 * @param description The profile description HasMap to update
 * @param documentId The document ID of the user's document
 * @return A Task that can be used to monitor the status of the operation
 */
fun FirebaseFirestore.insertProfileDescription(description: HashMap<String, Any>, documentId: String): Task<Void> =
    collection(Constants.KEY_COLLECTION_USERS).document(documentId).update(description)


/**
 * Suspends the current coroutine and retrieve the user data from FireStore database for the given email
 * @param email The email address of the user
 * @return A User object containing the user data, or null if the user is not found
 */
suspend fun FirebaseFirestore.getUserData(email: String): User? {
    return suspendCoroutine { count ->
        collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL, email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var userData: User? = null
                for(document in querySnapshot.documents){
                    userData = User(
                        id = document.id,
                        profileImage = document.getString(Constants.KEY_IMAGE).toString(),
                        name = document.getString(Constants.KEY_NAME).toString(),
                        description = document.getString(Constants.KEY_DESCRIPTION).toString()
                    )
                    break
                }
                count.resume(userData)
            }
            .addOnFailureListener { count.resume(null) }
    }
}

/**
 * A suspended function that retrieve a list of user object via the Firebase FireStore
 * @param currentUserId the ID of the current user
 * @return a list of User objects
 */
suspend fun FirebaseFirestore.getAllUsers(currentUserId: String): ArrayList<User> {

    // Uses a kotlin coroutines to perform an asynchronous operations
    return suspendCoroutine { count ->

        // Retrieve a snapshot of the users collection and handles the operation result in the addOnCompletedListener method
        collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->

                // If the operation is successful and returns some result
                if (task.isSuccessful && task.result != null) {

                    // Creates a list of User objects and adds each object to this list
                    val users: ArrayList<User> = arrayListOf()
                    for (queryDocumentSnapshot: QueryDocumentSnapshot in task.result) {

                        // Filters out the current user object and avoids its inclusion in the retrieved user list
                        if (currentUserId == queryDocumentSnapshot.id) continue

                        // Adds a new user object to the user list
                        val user = User(
                            id = queryDocumentSnapshot.id,
                            name = queryDocumentSnapshot.getString(Constants.KEY_NAME).toString(),
                            description = queryDocumentSnapshot.getString(Constants.KEY_DESCRIPTION)
                                .toString(),
                            profileImage = queryDocumentSnapshot.getString(Constants.KEY_IMAGE)
                                .toString(),
                            token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN)
                                .toString()
                        )
                        users.add(user)
                    }

                    // Resolves the coroutine suspension and returns the retrieved user list
                    count.resume(users)
                }
            }
    }
}

/**
 * Updates the Firebase Cloud Messaging (FCM) token for the user document with the given document path
 * @param documentPath the path to the user document
 * @param token teh new FCM token to be set for the user
 * @return a Task<Void> representing the status of the update operation
 */
fun FirebaseFirestore.updateToken(documentPath: String, token: String): Task<Void> =
    collection(Constants.KEY_COLLECTION_USERS)
        .document(documentPath)
        .update(Constants.KEY_FCM_TOKEN, token)

/**
 * Adds a new message to the FireStore "chats" collection
 * @param message a HashMap object containing the message data
 */
fun FirebaseFirestore.sedMessage(message: HashMap<String, Any>) =
    collection(Constants.KEY_COLLECTION_CHAT).add(message)

/**
 * Listens for new chat messages between two users and calls a callback function with the messages
 * @param userId the ID of the user who is sending the messages
 * @param receiverUserId the ID of the user who is receiving the messages
 * @param callbackL a lambda function that takes an ArrayList of Chat objects as an argument
 */
fun FirebaseFirestore.listenerMessage(
    userId: String,
    receiverUserId: String,
    callbackL: (ArrayList<Chat>) -> Unit
) {

   collection(Constants.KEY_COLLECTION_CHAT)
       .whereIn(Constants.KEY_SENDER_ID, listOf(userId, receiverUserId))
       .whereIn(Constants.KEY_RECEIVER_ID, listOf(userId, receiverUserId))
       .addSnapshotListener(eventListener(callbackL))

}

/**
 * Creates an EventListener for FireStore snapshot listeners that returns an ArrayList of Chat objects to a callback function.
 * @param callback a lambda function that takes an ArrayList of Chat objects as an argument
 * @return an EventListener<QuerySnapshot> object
 */
private fun eventListener(callback: (ArrayList<Chat>) -> Unit): EventListener<QuerySnapshot> =
    EventListener<QuerySnapshot> { value, error ->
        // Create an empty ArrayList to hold the Chat objects
        val messages: ArrayList<Chat> = arrayListOf()

        // Check for errors
        if (error != null) return@EventListener

        value?.documentChanges?.forEach { documentChange ->
            if(documentChange.type == DocumentChange.Type.ADDED)
                messages.add(documentChange.document.toChat())
        }

        // Call the callback function with the ArrayList of Chat objects
        callback(messages)

    }

// Converts a Firebase FireStore DocumentSnapshot object to a custom Chat object
private fun DocumentSnapshot.toChat(): Chat = Chat(
    senderId = getString(Constants.KEY_SENDER_ID).toString(),
    receiverId = getString(Constants.KEY_RECEIVER_ID).toString(),
    message = getString(Constants.KEY_MESSAGE).toString(),
    dateTime = getReadableDateTime(getDate(Constants.KEY_DATE_TIME)!!),
    dateObject = getTimestamp(Constants.KEY_TIMESTAMP)?.toDate() ?: Date(),
    messageType = getString(Constants.KEY_TYPE_MESSAGE).toString(),
    imageUrl = getString(Constants.KEY_IMAGE_URL).toString()
)

/**
 * Return a formatted date string for give Date object
 * @param date the Date object to format
 * @return a formatted date string
 */
private fun getReadableDateTime(date: Date): String {
    return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
}

/**
 * Listens to the availability status of a receiver user
 * @param receiverUserId the id of the receiver user
 * @param listener a lambda function that will be called when the availability status changes.
 *                 It takes an integer parameter which represents the new availability status
 */
fun FirebaseFirestore.listenerAvailabilityOfReceiver(receiverUserId: String, listener: (Int) -> Unit){
    collection(Constants.KEY_COLLECTION_USERS).document(receiverUserId)
        .addSnapshotListener{ value, _ ->
            if(value != null && value.exists()){
                val availability: Long = value.get(Constants.KEY_AVAILABILITY) as Long
                listener(availability.toInt())
            }
        }
}


/**
 * This function extends the FirebaseFireStore class to  search for conversations in a specific collection in the FireStore database
 * @param senderId The ID of the conversation sender
 * @param receiverId The ID of the conversation receiver
 * @return A Task that returns a QuerySnapshot containing the document that meet the specified query criteria
 */
fun FirebaseFirestore.checkForConversationRemotely(senderId: String, receiverId: String): Task<QuerySnapshot> =
    collection(Constants.KEY_COLLECTION_CONVERSATIONS)
        .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
        .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
        .get()


/**
 * Adds a nre conversation document to a specific collection in the FireStore database
 * @param conversation A HashMap containing the conversation data to be added as fields in the new document
 * @return A String representing the ID of the new document that was added to the conversation collection
 */
suspend fun FirebaseFirestore.addConversation(conversation: HashMap<String, Any>): String = suspendCoroutine { count ->
    collection(Constants.KEY_COLLECTION_CONVERSATIONS)
        .add(conversation)
        .addOnSuccessListener { documentReference ->
            count.resume(documentReference.id)
        }
}

/**
 * Updates the timestamp field of a conversation document in the FireStore database with the current date and time
 * @param documentId A String representing the ID of the conversation document to be updated
 */
fun FirebaseFirestore.updateConversation(documentId: String){
    collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(documentId)
        .update(Constants.KEY_TIMESTAMP, Date())
}

/**
 * Listens to changes in the conversations collection of the FireStore database for recent conversations involving the specified sender user ID
 * @param senderId A String representing the ID of the sender id whose recent conversations are being listened to
 * @param listener A lambda function that takes an ArrayList of RecentConversation objects as a parameter, This function is called when changes to the conversations collection occur
 */
fun FirebaseFirestore.listenerRecentConversations(senderId: String, listener: (ArrayList<RecentConversation>) -> Unit){
    collection(Constants.KEY_COLLECTION_CONVERSATIONS)
        .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
        .addSnapshotListener(eventListenerRecentConversation(senderId, listener))
    collection(Constants.KEY_COLLECTION_CONVERSATIONS)
        .whereEqualTo(Constants.KEY_RECEIVER_ID, senderId)
        .addSnapshotListener(eventListenerRecentConversation(senderId, listener))
}

val recentConversations: ArrayList<RecentConversation> = arrayListOf()

/**
 * Returns an event listener for listening to recent conversations
 * @param senderId The id of the sender user
 * @param listener a lambda function that will be called when recent conversation changes
 * @return It takes an ArrayList parameter which represents the new list of recent conversations
 */
private fun eventListenerRecentConversation(senderId: String, listener: (ArrayList<RecentConversation>) -> Unit): EventListener<QuerySnapshot> =
    EventListener<QuerySnapshot>{ value, error ->

        if(error != null) return@EventListener

        if(value != null){
            for(documentChange: DocumentChange in value.documentChanges){
                if(documentChange.type == DocumentChange.Type.ADDED){
                    // Geta data from the document change and create a recent conversation object
                    val senderIdDocument = documentChange.document.getString(Constants.KEY_SENDER_ID)
                    val receiverId = documentChange.document.getString(Constants.KEY_RECEIVER_ID)
                    if(senderIdDocument == senderId){
                        val recentConversation = RecentConversation(
                            senderId = senderIdDocument.toString(),
                            receiverId = receiverId.toString(),
                            profileImage = documentChange.document.getString(Constants.KEY_RECEIVER_IMAGE).toString(),
                            name = documentChange.document.getString(Constants.KEY_RECEIVER_NAME).toString(),
                            description = documentChange.document.getString(Constants.KEY_RECEIVER_DESCRIPTION).toString(),
                            token = documentChange.document.getString(Constants.KEY_RECEIVER_FCM_TOKEN).toString(),
                            dateObject = documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!
                        )
                        recentConversations.add(recentConversation)
                    } else {
                        val recentConversation = RecentConversation(
                            senderId = receiverId.toString(),
                            receiverId = senderIdDocument.toString(),
                            profileImage = documentChange.document.getString(Constants.KEY_SENDER_IMAGE).toString(),
                            name = documentChange.document.getString(Constants.KEY_SENDER_NAME).toString(),
                            description = documentChange.document.getString(Constants.KEY_SENDER_DESCRIPTION).toString(),
                            token = documentChange.document.getString(Constants.KEY_SENDER_FCM_TOKEN).toString(),
                            dateObject = documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!
                        )
                        recentConversations.add(recentConversation)
                    }
                } else if (documentChange.type == DocumentChange.Type.MODIFIED){
                    // update the date object of the corresponding recent conversation
                    for (i in 0 until recentConversations.size){
                        val senderIdDocument = documentChange.document.getString(Constants.KEY_SENDER_ID)
                        val receiverId = documentChange.document.getString(Constants.KEY_RECEIVER_ID)
                        if (recentConversations[i].senderId == senderIdDocument && recentConversations[i].receiverId == receiverId){
                            recentConversations[i].dateObject = documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!
                            break
                        }
                    }
                }
            }

            // Call the listener with the updated recent conversations list
            listener(recentConversations)
        }

    }