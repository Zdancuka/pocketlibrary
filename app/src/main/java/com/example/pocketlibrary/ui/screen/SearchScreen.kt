package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import coil3.compose.AsyncImage
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.screen.element.OutlinedTag
import com.example.pocketlibrary.ui.screen.element.SearchBar
import com.example.pocketlibrary.ui.screen.element.SwipeToDeleteCard
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

const val MAX_TAGS_TO_SHOW = 3

@Composable
fun SearchScreen(
    bookViewModel: BookViewModel,
    onBookClick: (BookWithTags) -> Unit = { }
) {

    // query and filteredBooks come from the ViewModel (via combine) instead of being
    // computed here on every recomposition — see BookViewModel.searchQuery / filteredBooks.
    val query by bookViewModel.searchQuery.collectAsState()
    val filteredBooks by bookViewModel.filteredBooks.collectAsState()

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
                text = stringResource(R.string.search_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            SearchBar(
                query = query,
                onQueryChange = { bookViewModel.onSearchQueryChange(it) }
            )
        }

        item {
            Text(
                text =
                    if (query.isBlank())
                        stringResource(R.string.no_text_yet)
                    else
                        stringResource(R.string.books_with_word) + " \"$query\"",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }


        items(filteredBooks, key = {it.book.bookId}) { bookWithTags ->
            SwipeToDeleteCard(
                bookWithTags = bookWithTags,
                bookViewModel = bookViewModel
            ) {
                BookTagCard(
                    bookWithTags = bookWithTags,
                    onClick = { onBookClick(bookWithTags) }
                )
            }
        }

        if (filteredBooks.isEmpty()){
            item {
                Text(
                    text = stringResource(R.string.no_books_yet),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.SpaceXLarge),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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

            AsyncImage(
                model = book.imageUri?: R.drawable.book_caver_example,
                contentDescription = null,
                modifier = Modifier
                    .width(Dimens.BookCoverWidthSmall)
                    .height(Dimens.BookCoverHeightSmall)
                    .clip(RoundedCornerShape(Dimens.CornerSmall)),
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
                            modifier = Modifier.padding(Dimens.SpaceXXSmall),
                            text = stringResource(R.string.more),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    }
}