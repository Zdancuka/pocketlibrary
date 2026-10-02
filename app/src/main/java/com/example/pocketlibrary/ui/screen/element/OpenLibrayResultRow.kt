package com.example.pocketlibrary.ui.screen.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.remote.openlibrary.OpenLibraryDoc
import com.example.pocketlibrary.data.remote.openlibrary.authorDisplay
import com.example.pocketlibrary.data.remote.openlibrary.coverUrl
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun OpenLibraryResultRow(
    doc: OpenLibraryDoc,
    onClick: () -> Unit
){
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.CornerXSmall))
            .clickable{onClick()},
        color = MaterialTheme.colorScheme.surfaceVariant
    ){
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.CornerSmall))
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimens.SpaceSmall),
            verticalAlignment = Alignment.CenterVertically
        ){
            val coverUrl = doc.coverUrl()

            val painter = rememberAsyncImagePainter(
                model = coverUrl ?: R.drawable.book_caver_example,
                onState = { state ->
                    android.util.Log.d("CoverDebug", "title=${doc.title} url=$coverUrl state=$state")
                }
            )

            Image(
                painter = painter,
                contentDescription = doc.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.CornerSmall))
                    .width(Dimens.BookCoverWidthMedium)
                    .height(Dimens.BookCoverHeightMedium)
            )


            Spacer(modifier = Modifier.width(Dimens.SpaceXSmall))

            Column {
                Text (
                    text = doc.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (doc.authorDisplay().isNotBlank()){
                    Text (
                        text = doc.authorDisplay(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}