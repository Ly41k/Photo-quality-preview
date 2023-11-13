package com.example.photoqualitypreview.presentation.choose

import android.net.Uri
import android.util.Log
import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.domain.PhotoItem
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

class ChooseViewModel : ViewModel() {

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
        is ChooseScreenPartialState.Navigate -> prevState.copy(navigateEvent = Event(changes.action))
        is ChooseScreenPartialState.PhotoPicked -> prevState.copy(
            photoItem = changes.photoItem,
            isNextButtonActive = true
        )
    }

    val state = _state

    fun onEvent(event: ChooseScreenEvent) {
        when (event) {
            ChooseScreenEvent.OnAddPhotoClicked -> addPhotoClicks.tryEmit(Unit)
            ChooseScreenEvent.OnNextButtonClicked -> nextBtnClicks.tryEmit(Unit)
            is ChooseScreenEvent.OnPhotoPicked -> photoPicks.tryEmit(event.bytes)
        }
    }

    private fun addPhotoClicksFlow(): Flow<ChooseScreenPartialState> =
        addPhotoClicks.map { ChooseScreenPartialState.Navigate(ChooseScreenNavigationAction.PickImage) }

    private fun nextButtonClicksFlow(): Flow<ChooseScreenPartialState> =
        nextBtnClicks.map { ChooseScreenPartialState.Navigate(ChooseScreenNavigationAction.CompareScreen(Uri.EMPTY)) }

    private fun photoPicksFlow(): Flow<ChooseScreenPartialState> =
        photoPicks.map { ChooseScreenPartialState.PhotoPicked(PhotoItem(it)) }


    private fun handleThrowable(error: Throwable) {
        Log.d("TESTING_TAG", "handleThrowable - $error")
    }
}