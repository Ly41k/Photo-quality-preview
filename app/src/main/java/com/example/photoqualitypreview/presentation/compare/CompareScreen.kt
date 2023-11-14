package com.example.photoqualitypreview.presentation.compare

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photoqualitypreview.domain.PhotoItem
import com.example.photoqualitypreview.presentation.views.PhotoView
import com.example.photoqualitypreview.ui.theme.PhotoQualityPreviewTheme

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
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Modify the photo",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        PhotoView(
            photoBytes = if (state.qualityPercent < 100) state.modifiedItem?.photoBytes else state.originalItem?.photoBytes,
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Slider(
            value = state.qualityPercent.toFloat(),
            onValueChange = {
                Log.d("TESTING_TAG", "onValueChange - $it")
                onEvent(CompareScreenEvent.OnQualityChanged(it))
            },
            enabled = state.isSliderActive,
            valueRange = 1f..100f,
            steps = 10,
            onValueChangeFinished = {
                onEvent(CompareScreenEvent.OnQualityChangeFinished)
            }


        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Original: ${state.originalSize} ")
        Text(text = "Modified: ${state.modifiedSize}")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEvent(CompareScreenEvent.OnNextButtonClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }


}

//@Preview(showBackground = true)
//@Composable
//fun CompareScreenPreview() {
//    PhotoQualityPreviewTheme {
//        CompareScreen(
//            state = CompareScreenState(
//                PhotoItem(null, null), 100f, ""
//            ), {
//
//            }
//        )
//    }
//}