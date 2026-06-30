package com.example.pocketlibrary.ui.theme

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
import androidx.compose.ui.unit.dp

data class Book(
    val title: Int,
    val author: Int,
    val imageRes: Int,
)

@Composable
fun LibraryScreen(){

    val books: List<Book> = listOf(
        Book(
            title = R.string.book_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),
        Book(
            title = R.string.book_long_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),
        Book(
            title = R.string.book_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),
        Book(
            title = R.string.book_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),Book(
            title = R.string.book_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),Book(
            title = R.string.book_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),Book(
            title = R.string.book_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),Book(
            title = R.string.book_title,
            author = R.string.author_name,
            imageRes = R.drawable.book_caver_example,
        ),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {

            Image(
                painter = painterResource(book.imageRes),
                contentDescription = stringResource(book.title),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

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