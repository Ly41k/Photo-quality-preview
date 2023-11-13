package com.example.photoqualitypreview.presentation.choose

import com.example.photoqualitypreview.domain.PhotoItem

sealed interface ChooseScreenPartialState {
    data class Navigate(val action: ChooseScreenNavigationAction) : ChooseScreenPartialState
    data class PhotoPicked(val photoItem: PhotoItem) : ChooseScreenPartialState
}