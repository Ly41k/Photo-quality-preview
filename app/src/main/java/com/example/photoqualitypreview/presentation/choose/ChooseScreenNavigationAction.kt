package com.example.photoqualitypreview.presentation.choose

sealed interface ChooseScreenNavigationAction {
    data class CompareScreen(val filePath: String?) : ChooseScreenNavigationAction
    data object PickImage : ChooseScreenNavigationAction
}