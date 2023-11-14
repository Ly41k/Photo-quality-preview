package com.example.photoqualitypreview.presentation.compare

import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.domain.PhotoItem

data class CompareScreenState(
    val originalItem : PhotoItem? = null,
    val modifiedItem : PhotoItem? = null,

    val originalPhoto: ByteArray? = null,
    val originalFilePath: String? = null,

    val modifiedPhoto: ByteArray? = null,
    val modifiedFilePath: String? = null,

    val isSliderActive: Boolean = false,
    val qualityPercent: Int = 100,
    val originalSize: String? = null,
    val modifiedSize: String? = null,
    val navigateEvent: Event<CompareScreenNavigationAction>? = null
)