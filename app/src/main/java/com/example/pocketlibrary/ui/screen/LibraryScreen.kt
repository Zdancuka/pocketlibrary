package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.pocketlibrary.R

//If it is const need to be declared here like this.
private const val COLUMN_NUMBER = 2

@Composable
fun LibraryScreen(
    bookViewModel: BookViewModel,
    onBookClick: (BookWithTags) -> Unit = { }
){

    val books by bookViewModel.books.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(COLUMN_NUMBER),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Dimens.SpaceMedium),
        contentPadding = PaddingValues(Dimens.SpaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)
    ){
        items (books) {bookWithTags ->
            BookCard(
                bookWithTags = bookWithTags,
                onClick = { onBookClick(bookWithTags) }
            )
        }

        if (books.isEmpty()){
            item ( span = { GridItemSpan(maxLineSpan)}){
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
fun BookCard(
    bookWithTags: BookWithTags,
    onClick: (BookWithTags) -> Unit = {}
) {

    val book = bookWithTags.book

    Card (
        modifier = Modifier
            .fillMaxWidth()
            // It wasn't opening details because the tap never called the callback from LibraryScreen.
            .clickable { onClick(bookWithTags) },
        elevation = CardDefaults.cardElevation(Dimens.SpaceXXSmall),
        shape = RoundedCornerShape(Dimens.CornerLarge),
    ) {

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Dimens.SpaceXSmall)
        ) {

            Image(
                painter = painterResource(R.drawable.book_caver_example),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.BookCoverHeightLibrary)
                    .clip(RoundedCornerShape(Dimens.CornerSmall)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

            Text(
                text = book.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "by ${book.author}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}