package com.example.pocketlibrary.ui.screen.element

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun BookFormHeader(
    screenTitle: String,
    selectedImageUri: Uri?,
    onImageClick: () -> Unit
) {
    Text(
        text = screenTitle,
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(Dimens.SpaceXXLarge))

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(Dimens.BookCoverWidthMedium)
                .height(Dimens.BookCoverHeightMedium)
                .clip(RoundedCornerShape(Dimens.CornerLarge))
                .background(MaterialTheme.colorScheme.background)
                .border(
                    Dimens.BorderThin,
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(Dimens.CornerLarge)
                )
                .clickable{onImageClick ()},
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimens.CornerLarge)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_image_placeholder),
                    contentDescription = null,
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
}
