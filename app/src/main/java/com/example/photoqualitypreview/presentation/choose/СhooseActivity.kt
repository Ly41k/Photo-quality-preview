package com.example.photoqualitypreview.presentation.choose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.photoqualitypreview.core.ImagePicker
import com.example.photoqualitypreview.core.ImagePickerFactory
import com.example.photoqualitypreview.core.launchAndRepeatWithViewLifecycle
import com.example.photoqualitypreview.presentation.compare.CompareActivity
import com.example.photoqualitypreview.ui.theme.PhotoQualityPreviewTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch

class ChooseActivity : ComponentActivity() {

    private var _imagePicker: ImagePicker? = null
    private val imagePicker: ImagePicker get() = _imagePicker!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoQualityPreviewTheme {

                val viewModel = getViewModel(
                    key = "choose-screen",
                    factory = viewModelFactory { ChooseViewModel() }
                )

                val state by viewModel.state.collectAsState()
                _imagePicker = ImagePickerFactory().createPicker()
                subscribeViewModelEvents(viewModel)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChooseScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        imagePicker = imagePicker
                    )
                }
            }
        }
    }

    private fun subscribeViewModelEvents(viewModel: ChooseViewModel) {
        launchAndRepeatWithViewLifecycle { launch { viewModel.state.collect(::processNavigation) } }
    }

    private fun processNavigation(newViewState: ChooseScreenState) {
        val action = newViewState.navigateEvent?.getContentIfNotHandled() ?: return

        when (action) {
            ChooseScreenNavigationAction.PickImage -> imagePicker.pickImage()
            is ChooseScreenNavigationAction.CompareScreen -> navigateToCompare(action.url)
        }
    }

    private fun navigateToCompare(photoUrl: Uri) {
        val intent = Intent(this, CompareActivity::class.java)
        this.startActivity(intent)
    }

    override fun onDestroy() {
        _imagePicker = null
        super.onDestroy()
    }
}
