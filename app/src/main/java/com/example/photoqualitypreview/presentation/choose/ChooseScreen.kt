package com.example.photoqualitypreview.presentation.choose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photoqualitypreview.core.ImagePicker
import com.example.photoqualitypreview.presentation.views.PhotoView
import com.example.photoqualitypreview.ui.theme.PhotoQualityPreviewTheme

@Composable
fun ChooseScreen(
    state: ChooseScreenState?,
    onEvent: (ChooseScreenEvent) -> Unit,
    imagePicker: ImagePicker?,
    modifier: Modifier = Modifier,
) {

    imagePicker?.registerPicker { imageBytes -> onEvent(ChooseScreenEvent.OnPhotoPicked(imageBytes)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Choose photo",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box {
            if (state?.imageBytes == null) {
                Box(modifier = Modifier
                    .size(250.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { onEvent(ChooseScreenEvent.OnAddPhotoClicked) }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else {
                PhotoView(
                    photoBytes = state.imageBytes,
                    modifier = Modifier
                        .size(250.dp)
                        .clickable { onEvent(ChooseScreenEvent.OnAddPhotoClicked) }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onEvent(ChooseScreenEvent.OnNextButtonClicked) },

            modifier = Modifier.fillMaxWidth(),
            enabled = state?.isNextButtonActive == true
        ) {
            Text(text = "Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseScreenPreview() {
    PhotoQualityPreviewTheme { ChooseScreen(null, {}, null) }
}