package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

const val MAX_TAGS_TO_SHOW = 3

@Composable
fun TagScreen(
    bookViewModel: BookViewModel,
    onBookClick: (BookWithTags) -> Unit = { }
) {

    val books by bookViewModel.books.collectAsState()
    var query by remember { mutableStateOf("") }

    val filteredBooks = if (query.isBlank()){
        books
    }else {
        books.filter { bookWithTags ->
            bookWithTags.tags.any{tag->
                tag.name.contains(query, ignoreCase = true)
            }
        }
    }

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

        item { TagSearchBar(
            query = query,
            onQueryChange = {query = it}
            )
        }


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
////            }
//        // Todo search history
//
//        item {
//            Text(
//                text = stringResource(R.string.show_more),
//                modifier = Modifier.padding(top = Dimens.SpaceXSmall),
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface,
//                textDecoration = TextDecoration.Underline
//            )
//        }

        item {
            Text(
                text =
                    if (query.isBlank())
                        stringResource(R.string.books_with_tag)
                    else
                        stringResource(R.string.books_with_tag) + " \"$query\" ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }


        items(filteredBooks) { bookWithTags ->
            BookTagCard(
                bookWithTags = bookWithTags,
                onClick = { onBookClick(bookWithTags) }
            )
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