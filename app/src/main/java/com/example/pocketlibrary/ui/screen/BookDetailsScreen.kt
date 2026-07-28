package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun BookDetailsScreen(bookWithTags: BookWithTags)
{
    val book = bookWithTags.book
    val tags = bookWithTags.tags

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            // Typo in drawable name (`caver`) - keeping as-is until resource is renamed.
            painter = painterResource(R.drawable.book_caver_example), //ToDo
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.DetailsHeaderImageHeight),
            contentScale = ContentScale.Crop,
            alpha = 0.25f
        )


        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.SpaceLarge)
                .verticalScroll(rememberScrollState())
        ) {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpaceLarge),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,

            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,) {
                    Icon(
                        painter = painterResource(R.drawable.ic_language),
                        null,
                        tint = MaterialTheme.colorScheme.onSurface
                        )
                    Text(
                        text = book.language ?: "",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Image(
                    painter = painterResource(R.drawable.book_caver_example),
                    contentDescription = null,
                    modifier = Modifier
                        .width(Dimens.BookCoverWidthMedium)
                        .height(Dimens.BookCoverHeightMedium)
                        .clip(RoundedCornerShape(Dimens.CornerSmall)),
                    contentScale = ContentScale.Crop
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_document),
                        null,
                        tint = MaterialTheme.colorScheme.onSurface
                        )
                    Text(
                        text = book.pageNumber?.toString() ?: "",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                )

            Text(
                text = book.author,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
                )

            Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))


                Text(
                    stringResource(R.string.book_details_tags_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(Dimens.SpaceXSmall))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall)
                ) {

                    tags.forEach { tag ->

                        Surface(
                            shape = RoundedCornerShape(Dimens.CornerSmall),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 0.dp
                        ) {
                            Text(
                                text = "#${tag.name}",
                                modifier = Modifier.padding(
                                    horizontal = Dimens.SpaceSmall,
                                    vertical = Dimens.SpaceXXSmall
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Dimens.SpaceXXSmall))

                Text(
                    text = stringResource(R.string.show_more),
                    modifier = Modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

                Text(
                    text = stringResource(R.string.book_details_description_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))

                Text(
                text = book.bookDescription ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))

                Text(
                    text = stringResource(R.string.book_details_notes_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

                Text(
                    text = book.bookNotes ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

            Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))
        }
    }
}
