package dev.dr10.ping.data.local.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class LocalImageStorageManager(
    private val context: Context
) {

    /**
     * Save an image to the local storage
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

    /**
     * Load the profile image using the [path]
     *
     * @param path The path of the image to load
     * @return [Bitmap] of the loaded image
     */
    fun loadImage(path: String): Bitmap = BitmapFactory.decodeFile(path)

}