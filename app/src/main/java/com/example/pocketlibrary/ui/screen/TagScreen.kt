package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.theme.Dimens
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

const val MAX_TAGS_TO_SHOW = 3

@Composable
fun TagScreen(
    bookViewModel: BookViewModel,
    onBookClick: (BookWithTags) -> Unit = { }
) {

    val books by bookViewModel.books.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                start = Dimens.SpaceXLarge,
                top = Dimens.SpaceXLarge,
                end = Dimens.SpaceXLarge
            ),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)
    ) {

        item {
            Text(
                text = stringResource(R.string.tags),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item { TagSearchBar() }


//        Text(
//            text = stringResource(R.string.tag_search_history),
//            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//
//        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
//
//        FlowRow (
//            horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall),
//            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
//        ) {
//
//
//            bookWithTags.tags.forEach { tag ->
//                OutlinedTag(tag.name)
//            }
        // Todo search history

        item {
            Text(
                text = stringResource(R.string.show_more),
                modifier = Modifier.padding(top = Dimens.SpaceXSmall),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = TextDecoration.Underline
            )
        }

        item {
            Text(
                text = stringResource(R.string.books_with_tag) + "", //Todo search results
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }


        items(books) { bookWithTags ->
            BookTagCard(
                bookWithTags = bookWithTags,
                onClick = { onBookClick(bookWithTags) }
            )
        }

        item { Spacer(modifier = Modifier.height(Dimens.SpaceLarge)) }
    }
}


@Composable
fun BookTagCard(
    bookWithTags: BookWithTags,
    onClick: (BookWithTags) -> Unit = {}
) {

    val book = bookWithTags.book
    val tags = bookWithTags.tags

    Card( modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerXLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.SpaceMedium)
    ) {

        Row(modifier = Modifier
            .clickable { onClick(bookWithTags)}
            .padding(Dimens.SpaceSmall)
        )
            {

            Image(
                painter = painterResource(R.drawable.book_caver_example),
                contentDescription = null,
                modifier = Modifier
                    .width(Dimens.BookCoverWidthSmall)
                    .height(Dimens.BookCoverHeightSmall),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Dimens.SpaceSmall))

            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceXXSmall))

                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall)
                ) {
                    tags.take(MAX_TAGS_TO_SHOW).forEach { tag->
                        OutlinedTag(tag.name)
                    }

                    if (tags.size > MAX_TAGS_TO_SHOW) {
                        Text(
                            text = stringResource(R.string.more),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    }
}