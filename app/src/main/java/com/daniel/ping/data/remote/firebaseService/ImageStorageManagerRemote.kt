package com.daniel.ping.data.remote.firebaseService

import android.net.Uri
import com.daniel.ping.domain.utilities.Constants
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Saves the profile image to Firebase Storage and returns the download URL of the uploaded image
 * @param profileImage The Uri of the profile image to be uploaded to Firebase Storage
 * @return The download URL of the newly uploaded profile image
 */
suspend fun FirebaseStorage.saveProfileImageToFirebase(profileImage: Uri, fileName: String): String{
    val imageRef = getReference(Constants.KEY_STORAGE_PROFILE_IMAGES).child(fileName)

    imageRef.putFile(profileImage).await()

    return imageRef.downloadUrl.await().toString()
}

/**
 * Sends an image to Firebase Storage and returns the download URL of the uploaded image
 * @param image The Uri of the image to be uploaded to Firebase Storage
 * @return The download URL of the newly uploaded image
 */
suspend fun FirebaseStorage.sendMessageWithImage(image: Uri): String {
    val fileName = UUID.randomUUID().toString()
    val imageRef = getReference(Constants.KEY_STORAGE_IMAGES).child(fileName)

    imageRef.putFile(image).await()

    return imageRef.downloadUrl.await().toString()
}