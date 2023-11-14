package com.example.photoqualitypreview.presentation.compare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.photoqualitypreview.core.Constants.PHOTO_URI
import com.example.photoqualitypreview.core.ImageStorage
import com.example.photoqualitypreview.ui.theme.PhotoQualityPreviewTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

class CompareActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.getStringExtra(PHOTO_URI)
        if (uri == null) this.onDestroy()
        setContent {
            PhotoQualityPreviewTheme {

                val viewModel = getViewModel(
                    key = "compare-screen",
                    factory = viewModelFactory {
                        CompareViewModel(ImageStorage(this), uri.orEmpty())
                    }
                )

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

    }


}


    