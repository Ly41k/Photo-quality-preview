package com.example.photoqualitypreview.presentation.choose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.photoqualitypreview.core.Constants.ORIGINAL_IMAGE_KEY
import com.example.photoqualitypreview.core.ImagePicker
import com.example.photoqualitypreview.core.ImagePickerFactory
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.core.launchAndRepeatWithViewLifecycle
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenNavigationAction.CompareScreen
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenNavigationAction.PickImage
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenState
import com.example.photoqualitypreview.presentation.compare.CompareActivity
import com.example.photoqualitypreview.ui.theme.PhotoQualityPreviewTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch

class ChooseActivity : ComponentActivity() {

    private var imagePicker: ImagePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoQualityPreviewTheme {
                val viewModel = getViewModel(
                    key = "choose-screen",
                    factory = viewModelFactory { ChooseViewModel(ImageStorage(applicationContext)) }
                )

                val state by viewModel.state.collectAsState()
                imagePicker = ImagePickerFactory().createPicker()
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
            PickImage -> imagePicker?.pickImage()
            is CompareScreen -> navigateToCompare(action.filePath)
        }
    }

    private fun navigateToCompare(filePath: String?) {
        val intent = Intent(this, CompareActivity::class.java)
            .apply { this.putExtra(ORIGINAL_IMAGE_KEY, filePath) }
        this.startActivity(intent)
    }

    override fun onDestroy() {
        imagePicker = null
        super.onDestroy()
    }
}
