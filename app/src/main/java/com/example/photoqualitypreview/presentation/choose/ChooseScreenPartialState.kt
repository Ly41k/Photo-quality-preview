package com.example.photoqualitypreview.presentation.choose

sealed interface ChooseScreenPartialState {
    data class Navigate(val action: ChooseScreenNavigationAction) : ChooseScreenPartialState
    data class PhotoPicked(val imageBytes: ByteArray?) : ChooseScreenPartialState
}