package com.example.photoqualitypreview.presentation.compare

sealed interface CompareScreenEvent {
    data class OnQualityChanged(val value: Float) : CompareScreenEvent

    data object OnQualityChangeFinished : CompareScreenEvent

    data class FilePathReceived(val filePath: String):CompareScreenEvent
    data object OnNextButtonClicked : CompareScreenEvent

}