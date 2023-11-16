package com.example.photoqualitypreview.presentation.choose

import android.util.Log
import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent.OnAddPhotoClicked
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent.OnNextButtonClicked
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent.OnPhotoPicked
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenNavigationAction.CompareScreen
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenNavigationAction.PickImage
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenPartialState
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenPartialState.Navigate
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenPartialState.PhotoPicked
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

class ChooseViewModel(
    private val imageStorage: ImageStorage
) : ViewModel() {

    private val addPhotoClicks =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val nextBtnClicks =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val photoPicks =
        MutableSharedFlow<ByteArray>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val _state: StateFlow<ChooseScreenState>
        get() = merge(
            addPhotoClicksFlow(),
            nextButtonClicksFlow(),
            photoPicksFlow()
        )
            .catch { throwable -> handleThrowable(throwable) }
            .runningFold(ChooseScreenState(), ::viewStateReducer)
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ChooseScreenState()
            )


    private fun viewStateReducer(
        prevState: ChooseScreenState,
        changes: ChooseScreenPartialState
    ): ChooseScreenState = when (changes) {
        is Navigate -> prevState.copy(navigateEvent = Event(changes.action))
        is PhotoPicked -> prevState.copy(imageBytes = changes.imageBytes, isNextButtonActive = true)
        is ChooseScreenPartialState.PhotoPickedError -> prevState
    }

    val state = _state

    fun onEvent(event: ChooseScreenEvent) {
        when (event) {
            OnAddPhotoClicked -> addPhotoClicks.tryEmit(Unit)
            OnNextButtonClicked -> nextBtnClicks.tryEmit(Unit)
            is OnPhotoPicked -> photoPicks.tryEmit(event.bytes)
        }
    }

    private fun addPhotoClicksFlow(): Flow<ChooseScreenPartialState> =
        addPhotoClicks.map { Navigate(PickImage) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun nextButtonClicksFlow(): Flow<ChooseScreenPartialState> =
        nextBtnClicks
            .map { state.value.imageBytes }
            .filterNotNull()
            .flatMapLatest {
                flow { emit(imageStorage.saveOriginalImage(it)) }
                    .map { Navigate(CompareScreen(it)) }
                    .catch {
                        handleThrowable(it)
                        ChooseScreenPartialState.PhotoPickedError
                    }
            }


    private fun photoPicksFlow(): Flow<ChooseScreenPartialState> =
        photoPicks.map { byteArray -> PhotoPicked(byteArray) }


    private fun handleThrowable(error: Throwable) {
        Log.d("TESTING_TAG", "handleThrowable - $error")
    }
}