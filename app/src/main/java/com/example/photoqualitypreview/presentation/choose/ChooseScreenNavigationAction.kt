package com.example.photoqualitypreview.presentation.choose

import android.net.Uri

sealed interface ChooseScreenNavigationAction {
    data class CompareScreen(val url: Uri) : ChooseScreenNavigationAction
    data object PickImage : ChooseScreenNavigationAction
}