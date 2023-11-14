package com.example.photoqualitypreview.presentation.choose.models

import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenNavigationAction

data class ChooseScreenState(
    val imageBytes: ByteArray? = null,
    val isNextButtonActive: Boolean = false,
    val navigateEvent: Event<ChooseScreenNavigationAction>? = null
)