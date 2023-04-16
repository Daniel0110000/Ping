package com.daniel.ping.data.remote

import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
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
suspend fun FirebaseFirestore.getAllUsers(currentUserId: String): ArrayList<User>{

    // Uses a kotlin coroutines to perform an asynchronous operations
    return suspendCoroutine { count ->

        // Retrieve a snapshot of the users collection and handles the operation result in the addOnCompletedListener methis
        collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->

                // If the operation is successful and returns some result
                if(task.isSuccessful && task.result != null){

                    // Creates a list of User objects and adds each object to this list
                    val users: ArrayList<User> = arrayListOf()
                    for (queryDocumentSnapshot: QueryDocumentSnapshot in task.result){

                        // Filters out the current user object and avoids its inclusion in the retrieved user list
                        if(currentUserId == queryDocumentSnapshot.id) continue

                        // Adds a new user object to the user list
                        val user = User(
                            id = queryDocumentSnapshot.id,
                            name = queryDocumentSnapshot.getString(Constants.KEY_NAME).toString(),
                            description = queryDocumentSnapshot.getString(Constants.KEY_DESCRIPTION).toString(),
                            profileImage = queryDocumentSnapshot.getString(Constants.KEY_IMAGE).toString()
                        )
                        users.add(user)
                    }

                    // Resolves the coroutine suspension and returns the retrieved user list
                    count.resume(users)
                }
            }
    }
}