package com.example.photoqualitypreview.presentation.compare

import android.net.Uri

sealed interface CompareScreenNavigationAction {
    data class PreviewScreen(val originalUrl: Uri, val modifiedUrl: Uri) : CompareScreenNavigationAction
}