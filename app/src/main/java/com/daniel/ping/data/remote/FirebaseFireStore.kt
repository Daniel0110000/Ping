package com.daniel.ping.data.remote

import com.daniel.ping.domain.utilities.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

// This is an extension of the FirebaseFireStore class that adds a function called insertProfileDescription
// The function take a HashMap called 'description' that will be used to create a document in FireStore
fun FirebaseFirestore.insertProfileDescription(description: HashMap<String, Any>): Task<DocumentReference> =

    // Access the users collection in FireStore using the constant 'KEY_COLLECTION_USERS'.
    // The result of the operation is a Task object that can be used to handle the results or errors.
    collection(Constants.KEY_COLLECTION_USERS).add(description)