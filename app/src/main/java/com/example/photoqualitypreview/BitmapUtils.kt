package com.example.photoqualitypreview

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? =
    remember(bytes) { bytes?.let { BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap() } }