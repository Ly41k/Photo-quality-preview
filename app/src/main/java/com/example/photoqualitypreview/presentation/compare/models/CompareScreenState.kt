package com.example.photoqualitypreview.presentation.compare.models

import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.core.formatAsFileSize
import com.example.photoqualitypreview.domain.PhotoItem

data class CompareScreenState(
    val originalItem: PhotoItem? = null,
    val modifiedItem: PhotoItem? = null,
    val originalFilePath: String? = null,
    val modifiedFilePath: String? = null,
    val isNextButtonActive: Boolean = false,
    val isSliderActive: Boolean = false,
    val qualityPercent: Int = 90,
    val originalSize: String? = null,
    val modifiedSize: String? = null,
    val navigateEvent: Event<CompareScreenNavigationAction>? = null
) {
    fun getDifferences(): String {
        val originalSize = originalItem?.photoBytes?.size
        val modifiedSize = modifiedItem?.photoBytes?.size

        if (originalSize != null && modifiedSize != null) {
            val percentage = (modifiedSize.toDouble() / originalSize) * 100
            val amountDifference = originalSize - modifiedSize

            return if (percentage > 100) {
                "(+${percentage.toInt() - 100}%/+${(amountDifference * -1).formatAsFileSize})"
            } else {
                "(${percentage.toInt() - 100}%/-${amountDifference.formatAsFileSize})"
            }
        }
        return ""
    }
}