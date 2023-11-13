package com.example.photoqualitypreview

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.photoqualitypreview.choose.ChooseScreenEvent

@Composable
fun ChooseScreen(
    photoItem: PhotoItem?,
    onEvent: (ChooseScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Box {
            if (photoItem?.photoBytes == null) {
                Box(modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(40))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { onEvent(ChooseScreenEvent.OnAddPhotoClicked) }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = RoundedCornerShape(40)
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
                    photoItem = photoItem,
                    modifier = Modifier
                        .size(250.dp)
                        .clickable { onEvent(ChooseScreenEvent.OnAddPhotoClicked) }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onEvent(ChooseScreenEvent.OnNextButtonClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }
}