package com.example.photoqualitypreview.core

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

class ImagePicker(private val activity: ComponentActivity) {
    private lateinit var getContent: ActivityResultLauncher<String>

    @SuppressLint("ComposableNaming")
    @Composable
    fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { activity.contentResolver.openInputStream(uri)?.use { onImagePicked(it.readBytes()) } }
        }
    }

    fun pickImage() {
        getContent.launch("image/*")
    }
}