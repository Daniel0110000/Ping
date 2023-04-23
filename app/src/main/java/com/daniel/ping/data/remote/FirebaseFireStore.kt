package com.daniel.ping.data.remote

import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// This is an extension of the FirebaseFireStore class that adds a function called insertProfileDescription
// The function take a HashMap called 'description' that will be used to create a document in FireStore
fun FirebaseFirestore.insertProfileDescription(description: HashMap<String, Any>): Task<DocumentReference> =

// Access the users collection in FireStore using the constant 'KEY_COLLECTION_USERS'.
    // The result of the operation is a Task object that can be used to handle the results or errors.
    collection(Constants.KEY_COLLECTION_USERS).add(description)

/**
 * A suspended function that retrieve a list of user object via the Firebase FireStore
 * @param currentUserId the ID of the current user
 * @return a list of User objects
 */
suspend fun FirebaseFirestore.getAllUsers(currentUserId: String): ArrayList<User> {

    // Uses a kotlin coroutines to perform an asynchronous operations
    return suspendCoroutine { count ->

        // Retrieve a snapshot of the users collection and handles the operation result in the addOnCompletedListener methis
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
    // Listen for messages sent by the current user to the receiver
    collection(Constants.KEY_COLLECTION_CHAT)
        .whereEqualTo(Constants.KEY_SENDER_ID, userId)
        .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUserId)
        .addSnapshotListener(eventListener(callbackL))

    // Listen for messages sent by the receiver to the current user
    collection(Constants.KEY_COLLECTION_CHAT)
        .whereEqualTo(Constants.KEY_SENDER_ID, receiverUserId)
        .whereEqualTo(Constants.KEY_RECEIVER_ID, userId)
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

        // Loop through the document changes in the snapshot
        if (value != null) {
            for (documentChange: DocumentChange in value.documentChanges) {
                // Only add new messages (not modifier or removed messages)
                if (documentChange.type == DocumentChange.Type.ADDED) {
                    // Create a new Chat object from the FireStore document data
                    val message = Chat(
                        senderId = documentChange.document.getString(Constants.KEY_SENDER_ID)
                            .toString(),
                        receiverId = documentChange.document.getString(Constants.KEY_RECEIVER_ID)
                            .toString(),
                        message = documentChange.document.getString(Constants.KEY_MESSAGE)
                            .toString(),
                        dateTime = getReadableDateTime(documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!),
                        dateObject = documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!
                    )
                    messages.add(message)
                }
            }

            // Call the callback function with the ArrayList of Chat objects
            callback(messages)

        }

    }

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