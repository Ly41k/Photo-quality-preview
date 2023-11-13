package com.example.photoqualitypreview.compare

import com.example.photoqualitypreview.PhotoItem

data class CompareScreenState(
    val photoItem: PhotoItem,
    val qualityPercent: Float,
    val originalSize: String,
) {

    fun getModifiedSize(): String {
        return ""
    }
}