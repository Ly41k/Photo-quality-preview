package com.example.photoqualitypreview.presentation.preview

import com.example.photoqualitypreview.presentation.compare.CompareScreenPartialState

sealed interface PreviewScreenPartialState {

    data class OriginalImageLoaded(val byteArray: ByteArray?) : PreviewScreenPartialState

    data class ModifiedImageLoaded(val byteArray: ByteArray?) : PreviewScreenPartialState
}