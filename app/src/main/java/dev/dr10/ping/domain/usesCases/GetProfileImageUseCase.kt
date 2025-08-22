package dev.dr10.ping.domain.usesCases

import android.graphics.Bitmap
import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.StorageRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class GetProfileImageUseCase(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(): Result<Bitmap, ErrorType> = try {
        // Get the path of the image to load
        val imagePath = authRepository.getProfileData()!!.profileImagePath
        // Load the image using the path
        val imageBitmap = storageRepository.loadProfileImage(imagePath)
        Result.Success(imageBitmap)
    } catch (e: Exception) {
        Log.e(this::class.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}