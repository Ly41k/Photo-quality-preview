package com.example.photoqualitypreview.presentation.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.photoqualitypreview.core.Constants
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.ui.theme.PhotoQualityPreviewTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

class PreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val originalFileName = intent.getStringExtra(Constants.ORIGINAL_IMAGE_KEY)
        val modifiedFileName = intent.getStringExtra(Constants.MODIFIED_IMAGE_KEY)
        if (originalFileName == null || modifiedFileName == null) {
            finish()
            return
        }
        setContent {
            PhotoQualityPreviewTheme {
                val viewModel = getViewModel(
                    key = "preview-screen",
                    factory = viewModelFactory { PreviewViewModel(ImageStorage(applicationContext)) }
                )
                viewModel.setFilePaths(originalFileName.orEmpty(), modifiedFileName.orEmpty())

                val state by viewModel.state.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PreviewScreen(state = state)
                }
            }
        }
    }

}