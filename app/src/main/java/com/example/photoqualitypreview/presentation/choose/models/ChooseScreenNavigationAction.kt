package com.example.photoqualitypreview.presentation.choose.models

sealed interface ChooseScreenNavigationAction {
    data class CompareScreen(val filePath: String?) : ChooseScreenNavigationAction
    data object PickImage : ChooseScreenNavigationAction
}