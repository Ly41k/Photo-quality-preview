package com.example.photoqualitypreview.presentation.compare

import android.net.Uri
import android.util.Log
import com.example.photoqualitypreview.core.Constants.SLIDER_DEBOUNCE
import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.domain.PhotoItem
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

class CompareViewModel(
    private val imageStorage: ImageStorage,
    private val filePath: String
) : ViewModel() {

    private val _originalFilePath = MutableStateFlow(filePath)
    private val originalFilePath = _originalFilePath

    private val nextBtnClicks =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val qualityChanges =
        MutableSharedFlow<Float>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val qualityChangeFinish =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val _state: StateFlow<CompareScreenState>
        get() = merge(
            originalImageFlow(),
//            nextButtonClicksFlow(),
            qualityChangesFlow(),
            qualityChangeFinishedFlow()

        )
            .catch { throwable -> handleThrowable(throwable) }
            .runningFold(CompareScreenState(), ::viewStateReducer)
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = CompareScreenState()
            )


    private fun viewStateReducer(
        prevState: CompareScreenState,
        changes: CompareScreenPartialState
    ): CompareScreenState = when (changes) {
        is CompareScreenPartialState.Navigate -> prevState.copy(navigateEvent = Event(changes.action))
        is CompareScreenPartialState.OriginalImageLoaded -> {
            prevState.copy(
                originalItem = PhotoItem(changes.byteArray, null),
                originalFilePath = filePath,
                originalSize = changes.byteArray?.size.toString(),
                isSliderActive = true
            )

        }

        is CompareScreenPartialState.QualityChanged -> {

            prevState.copy(
                qualityPercent = changes.value,
            )
        }

        is CompareScreenPartialState.ModifiedImageLoaded -> {
            prevState.copy(
                modifiedItem = PhotoItem(changes.byteArray, null),
                modifiedFilePath = changes.filePath,
                modifiedPhoto = changes.byteArray,
                modifiedSize = changes.byteArray?.size.toString()
            )
        }


    }

    val state = _state

    fun onEvent(event: CompareScreenEvent) {
        when (event) {
            CompareScreenEvent.OnNextButtonClicked -> nextBtnClicks.tryEmit(Unit)
            is CompareScreenEvent.OnQualityChanged -> qualityChanges.tryEmit(event.value)
            is CompareScreenEvent.FilePathReceived -> {
                originalFilePath.tryEmit(event.filePath)
            }
            is CompareScreenEvent.OnQualityChangeFinished ->{
                qualityChangeFinish.tryEmit(Unit)
            }
        }
    }


    private fun nextButtonClicksFlow(): Flow<CompareScreenPartialState> =
        nextBtnClicks.map {
            CompareScreenPartialState.Navigate(CompareScreenNavigationAction.PreviewScreen(Uri.EMPTY, Uri.EMPTY))
        }

    private fun originalImageFlow(): Flow<CompareScreenPartialState> =
        originalFilePath
            .map { imageStorage.getImage(it) }
            .map { CompareScreenPartialState.OriginalImageLoaded(it) }


    private fun qualityChangesFlow(): Flow<CompareScreenPartialState> =
        qualityChanges.map { CompareScreenPartialState.QualityChanged(it.toInt()) }

    private fun qualityChangeFinishedFlow(): Flow<CompareScreenPartialState> =
        qualityChangeFinish
            .onEach {
                Log.d("TESTING_TAG", "qualityChangeFinish")
            }.map {
                val count = state.value.qualityPercent
                val count1 = state.value.originalItem?.photoBytes
                imageStorage.saveModifiedImage(count1!!, count)
            }
            .map {
                Log.d("TESTING_TAG", "qualityChangeFinish - $it")
                imageStorage.getImage(it) to it
            }.onEach {
                Log.d("TESTING_TAG", "qualityChangeFinish - $it")
            }
            .map { CompareScreenPartialState.ModifiedImageLoaded(it.first, it.second) }



    private fun handleThrowable(error: Throwable) {
        Log.d("TESTING_TAG", "handleThrowable - $error")
    }


}