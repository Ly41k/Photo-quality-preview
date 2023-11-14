package com.example.photoqualitypreview.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.photoqualitypreview.core.Constants.MODIFIED_IMAGE
import com.example.photoqualitypreview.core.Constants.ORIGINAL_IMAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageStorage(private val context: Context) {
    suspend fun saveOriginalImage(bytes: ByteArray): String {
        return withContext(Dispatchers.IO) {
            val fileName = ORIGINAL_IMAGE
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream -> outputStream.write(bytes) }
            fileName
        }
    }

    suspend fun saveModifiedImage(bytes: ByteArray, quality: Int = 100): String {
        return withContext(Dispatchers.IO) {
            val fileName = MODIFIED_IMAGE
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.write(bytes)
            }
            fileName
        }
    }

    suspend fun getImage(fileName: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            context.openFileInput(fileName).use { inputStream -> inputStream.readBytes() }
        }
    }

    suspend fun deleteImage(fileName: String) {
        return withContext(Dispatchers.IO) {
            context.deleteFile(fileName)
        }
    }
}