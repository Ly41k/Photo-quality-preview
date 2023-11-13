package com.example.photoqualitypreview.presentation.choose

import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.domain.PhotoItem

data class ChooseScreenState(
    val photoItem: PhotoItem? = null,
    val isNextButtonActive: Boolean = false,
    val navigateEvent: Event<ChooseScreenNavigationAction>? = null
)