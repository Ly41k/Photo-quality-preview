package com.example.photoqualitypreview.choose

sealed interface ChooseScreenEvent {
    data object OnAddPhotoClicked : ChooseScreenEvent
    data object OnNextButtonClicked : ChooseScreenEvent
}
