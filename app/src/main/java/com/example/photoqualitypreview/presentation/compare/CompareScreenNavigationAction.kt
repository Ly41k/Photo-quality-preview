package com.example.photoqualitypreview.presentation.compare

sealed interface CompareScreenNavigationAction {
    data class PreviewScreen(val originalFileName: String?, val modifiedFileName: String?) :
        CompareScreenNavigationAction
}