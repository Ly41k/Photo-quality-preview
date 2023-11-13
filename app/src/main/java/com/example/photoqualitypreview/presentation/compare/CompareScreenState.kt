package com.example.photoqualitypreview.presentation.compare

import com.example.photoqualitypreview.domain.PhotoItem

data class CompareScreenState(
    val photoItem: PhotoItem,
    val qualityPercent: Float,
    val originalSize: String,
) {

    fun getModifiedSize(): String {
        return ""
    }
}