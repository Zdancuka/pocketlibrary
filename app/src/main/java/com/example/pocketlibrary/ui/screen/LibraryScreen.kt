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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.pocketlibrary.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.pocketlibrary.ui.theme.Dimens


@Composable
fun LibraryScreen(){

    val books: List<Book> = listOf(
        Book(
            title = R.string.book_title,
            author = R.string.author_name,
            language = R.string.book_en_language,
            pageNumber = R.string.book_page_number,
            bookDescription = R.string.book_description_example,
            bookNotes = R.string.book_notes_example,
            imageRes = R.drawable.book_caver_example,
            tags = listOf(R.string.tag_romantic,
                R.string.tag_scary,
                R.string.tag_idea,
                R.string.tag_dog,
                R.string.tag_funny,
                R.string.tag_life,
                R.string.tag_poem),
            ),
        Book(
            title = R.string.book_long_title,
            author = R.string.author_name,
            language = R.string.book_en_language,
            pageNumber = R.string.book_page_number,
            bookDescription = R.string.book_description_example,
            bookNotes = R.string.book_notes_example,
            imageRes = R.drawable.book_caver_example,
            tags = listOf(R.string.tag_romantic,
                R.string.tag_scary,
                R.string.tag_idea,
                R.string.tag_dog,
                R.string.tag_funny,
                R.string.tag_life,
                R.string.tag_poem),
            )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Dimens.SpaceMedium),
        contentPadding = PaddingValues(Dimens.SpaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)
    ){
        items ( books ) { book ->
            BookCard(book)
        }
    }
}

@Composable
fun BookCard(book: Book) {

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable() { },
        elevation = CardDefaults.cardElevation(Dimens.SpaceXXSmall),
        shape = RoundedCornerShape(Dimens.CornerLarge),
    ) {

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Dimens.SpaceXSmall)
        ) {

            Image(
                painter = painterResource(book.imageRes),
                contentDescription = stringResource(book.title),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.BookCoverHeightLibrary)
                    .clip(RoundedCornerShape(Dimens.CornerSmall)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

            Text(
                text = stringResource(book.title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "by ${stringResource(book.author)}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}