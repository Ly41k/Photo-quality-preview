package com.example.photoqualitypreview.core

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable
import com.example.photoqualitypreview.core.Constants.IMAGE_DIR
import java.io.FileNotFoundException

class ImagePicker(private val activity: ComponentActivity) {
    private lateinit var getContent: ActivityResultLauncher<String>

    @SuppressLint("ComposableNaming")
    @Composable
    fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        getContent = rememberLauncherForActivityResult(GetContent()) { uri ->
            uri?.let {
                try {
                    activity.contentResolver.openInputStream(uri)?.use {
                        onImagePicked(it.readBytes())
                    }
                } catch (e: FileNotFoundException) {
                    Log.e("TESTING_TAG", " registerPicker", e)
                }
            }
        }
    }

    fun pickImage() {
        getContent.launch(IMAGE_DIR)
    }
}