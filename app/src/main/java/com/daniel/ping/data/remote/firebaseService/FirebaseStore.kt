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
suspend fun FirebaseStorage.sendMessageWithFileOrMP3(fileName: String, file: Uri, isMP3: Boolean = false): String{
    val fileRef = getReference(if (!isMP3) Constants.KEY_STORAGE_FILES else Constants.KEY_STORAGE_MP3).child(fileName)

    fileRef.putFile(file).await()

    return fileRef.downloadUrl.await().toString()
}