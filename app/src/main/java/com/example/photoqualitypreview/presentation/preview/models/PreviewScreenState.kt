package com.example.photoqualitypreview.presentation.preview.models

import com.example.photoqualitypreview.domain.PhotoItem

data class PreviewScreenState(
    val originalItem: PhotoItem? = null,
    val modifiedItem: PhotoItem? = null,
    val originalSize: String? = null,
    val modifiedSize: String? = null
)

