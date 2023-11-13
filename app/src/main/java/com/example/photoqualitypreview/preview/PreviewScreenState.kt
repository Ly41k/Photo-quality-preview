package com.example.photoqualitypreview.preview

import com.example.photoqualitypreview.PhotoItem

data class PreviewScreenState(
    val originalPhotoItem: PhotoItem,
    val modifiedPhotoItem: PhotoItem,
    val originalPhotoSize: String,
    val modifiedPhotoSize: String
)

