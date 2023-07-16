package com.daniel.ping.data.local

import android.content.Context
import android.graphics.Bitmap
import java.io.File

object ImageStorageManagerLocal{
    /**
     * Saves an image to local storage an return the absolute path of the saved file
     * @param context The context of the application
     * @param image The Bitmap image to be saved
     * @param fileName The desired file name for the saved image
     * @return The absolute path of the saved image file
     */
    fun saveImageToLocalStorage(context: Context, image: Bitmap, fileName: String): String {
        val fileOutputStream = context.openFileOutput("$fileName.jpg", Context.MODE_PRIVATE)
        image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()

        val file = File(context.filesDir, "$fileName.jpg")
        return file.absolutePath
    }
}