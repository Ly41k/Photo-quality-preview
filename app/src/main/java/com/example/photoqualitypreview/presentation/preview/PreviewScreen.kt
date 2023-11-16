package com.example.photoqualitypreview.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.photoqualitypreview.presentation.preview.models.PreviewScreenState
import com.example.photoqualitypreview.presentation.views.PhotoView

@Composable
fun PreviewScreen(
    state: PreviewScreenState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Preview",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                stringResource(id = R.string.original_),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                stringResource(id = R.string.modified_),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(modifier = Modifier.padding(top = 16.dp)) {
            PhotoView(
                photoBytes = state.originalItem?.photoBytes,
                modifier = Modifier
                    .size(200.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            PhotoView(
                photoBytes = state.modifiedItem?.photoBytes,
                modifier = Modifier
                    .size(200.dp)
                    .weight(1f)
            )
        }

        Row(modifier = Modifier.padding(top = 8.dp, bottom = 50.dp)) {
            Text(
                text = state.originalSize.orEmpty(),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = state.modifiedSize.orEmpty(),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
