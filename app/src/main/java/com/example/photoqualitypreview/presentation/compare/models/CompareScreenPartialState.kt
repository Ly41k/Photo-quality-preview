package com.example.photoqualitypreview.presentation.compare.models

sealed interface CompareScreenPartialState {
    data class Navigate(val action: CompareScreenNavigationAction) : CompareScreenPartialState

    data class OriginalImageLoaded(val byteArray: ByteArray?) : CompareScreenPartialState

    data class ModifiedImageLoaded(val byteArray: ByteArray?, val filePath: String) : CompareScreenPartialState

    data class QualityChanged(val value: Int) : CompareScreenPartialState


}