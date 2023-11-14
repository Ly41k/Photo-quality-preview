package com.example.photoqualitypreview.presentation.preview

import android.util.Log
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.core.formatAsFileSize
import com.example.photoqualitypreview.domain.PhotoItem
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

class PreviewViewModel(
    private val imageStorage: ImageStorage,
) : ViewModel() {

    private val _filePaths = MutableStateFlow<Pair<String, String>?>(null)
    private val filePaths = _filePaths.filterNotNull()

    private val nextBtnClicks =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val qualityChanges =
        MutableSharedFlow<Float>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val qualityChangeFinish =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val _state: StateFlow<PreviewScreenState>
        get() = merge(
            originalImageFlow(),
            modifiedImageFlow()
        )
            .catch { throwable -> handleThrowable(throwable) }
            .runningFold(PreviewScreenState(), ::viewStateReducer)
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = PreviewScreenState()
            )

    private fun viewStateReducer(
        prevState: PreviewScreenState,
        changes: PreviewScreenPartialState
    ): PreviewScreenState = when (changes) {
        is PreviewScreenPartialState.OriginalImageLoaded -> {
            prevState.copy(
                originalItem = PhotoItem(changes.byteArray, null),
                originalSize = changes.byteArray?.size?.formatAsFileSize,
            )
        }

        is PreviewScreenPartialState.ModifiedImageLoaded -> {
            prevState.copy(
                modifiedItem = PhotoItem(changes.byteArray, null),
                modifiedSize = changes.byteArray?.size?.formatAsFileSize,
            )
        }
    }

    val state = _state

    fun setFilePaths(
        originalFilePath: String,
        modifiedFileName: String
    ) {
        _filePaths.tryEmit(Pair(originalFilePath, modifiedFileName))
    }


    private fun originalImageFlow(): Flow<PreviewScreenPartialState> =
        filePaths
            .map { imageStorage.getImage(it.first) }
            .map { PreviewScreenPartialState.OriginalImageLoaded(it) }

    private fun modifiedImageFlow(): Flow<PreviewScreenPartialState> =
        filePaths
            .map { imageStorage.getImage(it.second) }
            .map { PreviewScreenPartialState.ModifiedImageLoaded(it) }


    private fun handleThrowable(error: Throwable) {
        Log.d("TESTING_TAG", "handleThrowable - $error")
    }
}