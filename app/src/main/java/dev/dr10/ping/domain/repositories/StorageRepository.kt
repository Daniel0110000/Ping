package dev.dr10.ping.domain.repositories

interface StorageRepository {

    suspend fun uploadAndSaveProfileImage(image: ByteArray, imageName: String): String

    suspend fun downloadAndSaveProfileImage(imageName: String): String

}