package dev.dr10.ping.data.repositories

import android.graphics.Bitmap
import dev.dr10.ping.data.local.storage.LocalImageStorageManager
import dev.dr10.ping.domain.repositories.StorageRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.storage.Storage

class StorageRepositoryImpl(
    private val storageService: Storage,
    private val localStorageManager: LocalImageStorageManager
): StorageRepository {

    /**
     * Uploads an image to the Supabase storage and saves it to local storage
     *
     * @param image The image data as a ByteArray
     * @param imageName The name of the image file
     * @return The local path where the image is saved
     */
    override suspend fun uploadAndSaveProfileImage(image: ByteArray, imageName: String): String {
        storageService.from(Constants.PROFILE_IMAGE_BUCKET).upload(imageName, image) { upsert = false }
        return localStorageManager.saveImageToLocalStorage(image, imageName)
    }

    /**
     * Downloads the profile image from Supabase storage and saves it to local storage
     *
     * @param imageName The name of the image file to download
     * @return The local path where the image is saved
     */
    override suspend fun downloadAndSaveProfileImage(imageName: String): String {
        val profileImageByte = storageService.from(Constants.PROFILE_IMAGE_BUCKET).downloadAuthenticated(imageName)
        return localStorageManager.saveImageToLocalStorage(profileImageByte, imageName)
    }

    /**
     * Load the profile image from [localStorageManager] and return a [Bitmap] of the image
     */
    override suspend fun loadProfileImage(path: String): Bitmap = localStorageManager.loadImage(path)


}