package com.example.photoqualitypreview.presentation.preview.models


sealed interface PreviewScreenPartialState {

    data class OriginalImageLoaded(val byteArray: ByteArray?) : PreviewScreenPartialState

    data class ModifiedImageLoaded(val byteArray: ByteArray?) : PreviewScreenPartialState
}