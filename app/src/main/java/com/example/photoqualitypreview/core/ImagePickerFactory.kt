package com.example.photoqualitypreview.core

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class ImagePickerFactory {
    @Composable
    fun createPicker(): ImagePicker {
        val activity = LocalContext.current as ComponentActivity
        return remember(activity) { ImagePicker(activity) }
    }
}