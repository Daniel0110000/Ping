package dev.dr10.ping.domain.repositories

import android.graphics.Bitmap

interface StorageRepository {

    suspend fun uploadAndSaveProfileImage(image: ByteArray, imageName: String): String

    suspend fun downloadAndSaveProfileImage(imageName: String): String

    suspend fun loadProfileImage(path: String): Bitmap

}