package com.example.photoqualitypreview.presentation.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photoqualitypreview.R
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent.OnNextButtonClicked
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent.OnQualityChangeFinished
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenEvent.OnQualityChanged
import com.example.photoqualitypreview.presentation.compare.models.CompareScreenState
import com.example.photoqualitypreview.presentation.views.PhotoView

@Composable
fun CompareScreen(
    state: CompareScreenState,
    onEvent: (CompareScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.modify_the_photo),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )
        PhotoView(
            photoBytes = state.modifiedItem?.photoBytes ?: state.originalItem?.photoBytes,
            modifier = Modifier
                .size(250.dp)
                .padding(top = 16.dp)
                .aspectRatio(1f, true)
        )
        Slider(
            value = state.qualityPercent.toFloat(),
            onValueChange = { onEvent(OnQualityChanged(it)) },
            enabled = state.isSliderActive,
            valueRange = 1f..100f,
            steps = 100,
            onValueChangeFinished = { onEvent(OnQualityChangeFinished) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(R.string.original, state.originalSize.orEmpty()))
        Text(text = stringResource(R.string.modified, state.modifiedSize.orEmpty() ?: "", state.getDifferences()))

        Button(
            onClick = { onEvent(OnNextButtonClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 50.dp),
            enabled = state.isNextButtonActive
        ) { Text(stringResource(id = R.string.next)) }
    }
}