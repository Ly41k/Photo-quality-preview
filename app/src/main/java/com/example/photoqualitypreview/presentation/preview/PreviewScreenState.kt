package com.example.photoqualitypreview.presentation.preview

import com.example.photoqualitypreview.domain.PhotoItem

data class PreviewScreenState(
    val originalPhotoItem: PhotoItem,
    val modifiedPhotoItem: PhotoItem,
    val originalPhotoSize: String,
    val modifiedPhotoSize: String
)

