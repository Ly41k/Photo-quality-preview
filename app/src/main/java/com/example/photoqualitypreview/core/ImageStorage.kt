package com.example.photoqualitypreview.core

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.BitmapFactory
import com.example.photoqualitypreview.core.Constants.MODIFIED_IMAGE
import com.example.photoqualitypreview.core.Constants.ORIGINAL_IMAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImageStorage(private val context: Context) {
    suspend fun saveOriginalImage(bytes: ByteArray): String {
        return withContext(Dispatchers.IO) {
            val fileName = ORIGINAL_IMAGE
            context.openFileOutput(fileName, MODE_PRIVATE).use { outputStream -> outputStream.write(bytes) }
            fileName
        }
    }

    suspend fun saveModifiedImage(bytes: ByteArray, quality: Int): String {
        return withContext(Dispatchers.IO) {
            val fileName = MODIFIED_IMAGE
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            context.openFileOutput(fileName, MODE_PRIVATE).use { outputStream ->
                val stream = ByteArrayOutputStream()
                bitmap.compress(JPEG, quality, stream)
                val byteArray = stream.toByteArray()
                outputStream.write(byteArray)
            }
            fileName
        }
    }

    suspend fun getImage(fileName: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            context.openFileInput(fileName).use { inputStream -> inputStream.readBytes() }
        }
    }
}