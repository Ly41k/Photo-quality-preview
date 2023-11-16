package com.example.photoqualitypreview.presentation.compare

import android.util.Log
import com.example.photoqualitypreview.core.Event
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.core.formatAsFileSize
import com.example.photoqualitypreview.domain.PhotoItem
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent.OnNextButtonClicked
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent.OnQualityChangeFinished
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent.OnQualityChanged
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenNavigationAction.PreviewScreen
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenPartialState
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenPartialState.ModifiedImageLoaded
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenPartialState.Navigate
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenPartialState.OriginalImageLoaded
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenPartialState.QualityChanged
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

class CompareViewModel(
    private val imageStorage: ImageStorage,
) : ViewModel() {

    private val _originalFilePath = MutableStateFlow<String?>(null)
    private val originalFilePath = _originalFilePath.filterNotNull()

    private val nextBtnClicks =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val qualityChanges =
        MutableSharedFlow<Int>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val qualityChangeFinish =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val _state: StateFlow<CompareScreenState>
        get() = merge(
            originalImageFlow(),
            nextButtonClicksFlow(),
            qualityChangesFlow(),
            qualityChangesFlow1()
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
        is Navigate -> prevState.copy(navigateEvent = Event(changes.action))
        is OriginalImageLoaded -> {
            prevState.copy(
                originalItem = PhotoItem(changes.byteArray),
                originalFilePath = _originalFilePath.value,
                originalSize = changes.byteArray?.size?.formatAsFileSize,
                isSliderActive = changes.byteArray != null
            )
        }

        is QualityChanged -> prevState.copy(qualityPercent = changes.value)
        is ModifiedImageLoaded -> {
            prevState.copy(
                modifiedItem = PhotoItem(changes.byteArray),
                modifiedFilePath = changes.filePath,
                modifiedSize = changes.byteArray?.size?.formatAsFileSize,
                isNextButtonActive = changes.byteArray != null
            )
        }

        CompareScreenPartialState.ModifiedImageError -> {
            prevState
        }
    }

    val state = _state

    fun setOriginalFilePath(originalFilePath: String) {
        _originalFilePath.tryEmit(originalFilePath)
    }

    fun onEvent(event: CompareScreenEvent) {
        when (event) {
            OnNextButtonClicked -> nextBtnClicks.tryEmit(Unit)
            is OnQualityChanged -> qualityChanges.tryEmit(event.value.toInt())
            is OnQualityChangeFinished -> qualityChangeFinish.tryEmit(Unit)
        }
    }

    private fun nextButtonClicksFlow(): Flow<CompareScreenPartialState> =
        nextBtnClicks.map { Navigate(PreviewScreen(state.value.originalFilePath, state.value.modifiedFilePath)) }

    private fun originalImageFlow(): Flow<CompareScreenPartialState> =
        originalFilePath
            .map { imageStorage.getImage(it) }
            .map { OriginalImageLoaded(it) }


    private fun qualityChangesFlow(): Flow<CompareScreenPartialState> =
        qualityChanges.map { QualityChanged(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun qualityChangesFlow1(): Flow<CompareScreenPartialState> =
        qualityChanges
            .distinctUntilChanged()
            .flatMapLatest { quality ->
                flow {
                    val imageBytes = state.value.originalItem?.photoBytes
                    val fileName = imageBytes?.let { imageStorage.saveModifiedImage(it, quality) }.orEmpty()
                    val bytes = imageStorage.getImage(fileName)
                    emit(ModifiedImageLoaded(bytes, fileName))
                }.catch {
                    handleThrowable(it)
                    CompareScreenPartialState.ModifiedImageError
                }
            }

    private fun handleThrowable(error: Throwable) {
        Log.d("TESTING_TAG", "handleThrowable - $error")
    }
}