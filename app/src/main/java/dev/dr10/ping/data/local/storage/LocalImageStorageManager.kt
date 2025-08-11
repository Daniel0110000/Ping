package dev.dr10.ping.data.local.storage

import android.content.Context

class LocalImageStorageManager(
    private val context: Context
) {

    /**
     * Saves an image to the local storage
     *
     * @param image The image as a byte array
     * @param imageName The name of the image file to be saved
     * @return The absolute path of the saved image file
     */
    fun saveImageToLocalStorage(image: ByteArray, imageName: String): String {
        val file = context.getFileStreamPath(imageName)
        context.openFileOutput(imageName, Context.MODE_PRIVATE).use {
            it.write(image)
        }
        return file.absolutePath
    }

}