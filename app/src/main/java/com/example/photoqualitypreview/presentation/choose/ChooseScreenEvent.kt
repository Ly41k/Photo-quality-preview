package com.example.photoqualitypreview.presentation.choose

sealed interface ChooseScreenEvent {
    data object OnAddPhotoClicked : ChooseScreenEvent
    data object OnNextButtonClicked : ChooseScreenEvent
    data class OnPhotoPicked(val bytes: ByteArray) : ChooseScreenEvent
}
