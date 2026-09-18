package com.example.pocketlibrary.ui.screen.element

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
import coil3.compose.AsyncImage
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
        color = MaterialTheme.colorScheme.surface
    ){
        Row(
            modifier = Modifier
                .padding(Dimens.SpaceSmall),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = doc.coverUrl() ?: R.drawable.book_caver_example,
                contentDescription = doc.title,
                modifier = Modifier
                    .width(Dimens.BookCoverWidthMedium)
                    .height(Dimens.BookCoverHeightMedium)
                    .clip(RoundedCornerShape(Dimens.CornerSmall))
            )

            Spacer(modifier = Modifier.width(Dimens.SpaceXXSmall))

            Column {
                Text (
                    text = doc.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (doc.authorDisplay().isBlank()){
                    Text (
                        text = doc.authorDisplay(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}