package com.daniel.ping.data.remote.firebaseService

import android.net.Uri
import com.daniel.ping.domain.utilities.Constants
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

/**
 * Sends a file to Firebase Storage and returns the download URL of the uploaded file
 * @param fileName The name to be given to the file in Firebase Storage
 * @param file The uri of the file to be uploaded to Firebase Storage
 * @return The download URL of this newly uploaded file
 */
suspend fun FirebaseStorage.sendMessageWithFile(fileName: String, file: Uri): String{
    val fileRef = getReference(Constants.KEY_STORAGE_FILES).child(fileName)

    fileRef.putFile(file).await()

    return fileRef.downloadUrl.await().toString()
}