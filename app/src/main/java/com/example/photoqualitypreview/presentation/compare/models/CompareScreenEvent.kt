package com.example.photoqualitypreview.presentation.compare.models

sealed interface CompareScreenEvent {
    data class OnQualityChanged(val value: Float) : CompareScreenEvent
    data object OnQualityChangeFinished : CompareScreenEvent
    data object OnNextButtonClicked : CompareScreenEvent

}