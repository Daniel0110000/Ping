package dev.dr10.ping.domain.utils

import android.content.Context
import android.net.Uri
import dev.dr10.ping.domain.exceptions.ImageProcessingException

object ImageUtils {

    fun uriToByteArray(context: Context, uri: Uri): ByteArray =
        context.contentResolver.openInputStream(uri)?.use { inputStream -> inputStream.readBytes() }
            ?: throw ImageProcessingException(ErrorType.PROCESSING_IMAGE, "Failed to read image from URI: $uri")
}