package com.daniel.ping.data.remote.firebaseService

import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
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
                        profileImageUrl = document.getString(Constants.KEY_PROFILE_IMAGE_URL).toString(),
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