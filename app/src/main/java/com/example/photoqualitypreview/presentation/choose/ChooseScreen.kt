package com.example.photoqualitypreview.presentation.choose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.photoqualitypreview.core.ImagePicker
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent.OnAddPhotoClicked
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent.OnNextButtonClicked
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenEvent.OnPhotoPicked
import com.example.photoqualitypreview.presentation.choose.models.ChooseScreenState
import com.example.photoqualitypreview.presentation.views.PhotoView

@Composable
fun ChooseScreen(
    state: ChooseScreenState?,
    onEvent: (ChooseScreenEvent) -> Unit,
    imagePicker: ImagePicker?,
    modifier: Modifier = Modifier,
) {

    imagePicker?.registerPicker { imageBytes -> onEvent(OnPhotoPicked(imageBytes)) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        item {
            Text(
                text = stringResource(R.string.choose_photo),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Box {
                if (state?.imageBytes == null) {
                    Box(modifier = Modifier
                        .size(250.dp)
                        .aspectRatio(1f, true)
                        .padding(top = 20.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable { onEvent(OnAddPhotoClicked) }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.add),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                } else {
                    PhotoView(
                        photoBytes = state.imageBytes,
                        modifier = Modifier
                            .size(250.dp)
                            .aspectRatio(1f, true)
                            .padding(top = 20.dp)
                            .heightIn(min = 300.dp)
                            .clickable { onEvent(OnAddPhotoClicked) }
                    )
                }
            }
        }
        item {
            Button(
                onClick = { onEvent(OnNextButtonClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 50.dp),
                enabled = state?.isNextButtonActive == true
            ) { Text(text = stringResource(R.string.next)) }
        }
    }
}
