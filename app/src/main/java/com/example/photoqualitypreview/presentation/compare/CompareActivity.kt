package com.example.photoqualitypreview.presentation.compare

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
import com.example.photoqualitypreview.core.Constants.MODIFIED_IMAGE_KEY
import com.example.photoqualitypreview.core.Constants.ORIGINAL_IMAGE_KEY
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.core.launchAndRepeatWithViewLifecycle
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenNavigationAction.PreviewScreen
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenState
import com.example.photoqualitypreview.presentation.preview.PreviewActivity
import com.example.photoqualitypreview.ui.theme.PhotoQualityPreviewTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch

class CompareActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filePath = intent.getStringExtra(ORIGINAL_IMAGE_KEY)
        if (filePath == null) this.onDestroy()
        setContent {
            PhotoQualityPreviewTheme {
                val viewModel = getViewModel(
                    key = "compare-screen",
                    factory = viewModelFactory { CompareViewModel(ImageStorage(applicationContext)) }
                )

                viewModel.setOriginalFilePath(filePath.orEmpty())

                val state by viewModel.state.collectAsState()

                subscribeViewModelEvents(viewModel)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompareScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                    )
                }
            }
        }
    }

    private fun subscribeViewModelEvents(viewModel: CompareViewModel) {
        launchAndRepeatWithViewLifecycle { launch { viewModel.state.collect(::processNavigation) } }
    }

    private fun processNavigation(newViewState: CompareScreenState) {
        val action = newViewState.navigateEvent?.getContentIfNotHandled() ?: return

        when (action) {
            is PreviewScreen -> navigateToCompare(
                action.originalFileName,
                action.modifiedFileName
            )
        }
    }

    private fun navigateToCompare(originalFileName: String?, modifiedFileName: String?) {
        val intent = Intent(this, PreviewActivity::class.java)
            .apply { this.putExtra(ORIGINAL_IMAGE_KEY, originalFileName) }
            .apply { this.putExtra(MODIFIED_IMAGE_KEY, modifiedFileName) }
        this.startActivity(intent)
    }
}
    