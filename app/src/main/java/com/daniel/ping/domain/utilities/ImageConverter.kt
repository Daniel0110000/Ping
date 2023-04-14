package com.daniel.ping.domain.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImageConverter {
    companion object{

        // This function takes a Context and a Uri as input and returns a Bitmap object
        // It uses the Context to get a ContentResolver and opens an InputStream from the Uri
        // The InputStream is used to create a Bitmap object which is returned by the function
        fun uriToBitmap(context: Context, uri: Uri): Bitmap{
            val resolver = context.contentResolver
            val inputStream = resolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            return bitmap
        }

        // This function takes a Bitmap as input and returns an encoded String
        // It creates a scaled-down version of the Bitmap for display purposes
        // The scaled Bitmap is then compressed and encoded as a Base64 String which is returned by the function
        fun encodedImage(bitmap: Bitmap): String{
            val previewWith = 100
            val previewHeight = bitmap.height * previewWith / bitmap.width
            val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false)
            val byteArrayOutputStream = ByteArrayOutputStream()
            previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        // This function takes an encoded String as input and returns a Bitmap object
        // It decodes the String using Base64 and creates a Bitmap object from the decoded bytes
        fun decodeFromString(encodedImage: String): Bitmap{
            val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

    }
}