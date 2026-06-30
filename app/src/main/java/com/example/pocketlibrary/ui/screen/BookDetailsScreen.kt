package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.AssistChip
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
import androidx.compose.ui.unit.dp
import com.example.pocketlibrary.R

data class Book(
    val title: Int,
    val author: Int,
    val language: Int,
    val pageNumber: Int,
    val bookDescription: Int,
    val bookNotes: Int,
    val imageRes: Int,
    val tags: List<Int>,
)

val book = Book(
    title = R.string.book_title,
    author = R.string.author_name,
    language = R.string.book_en_language,
    pageNumber = R.string.book_page_number,
    bookDescription = R.string.book_description_example,
    bookNotes = R.string.book_notes_example,
    imageRes = R.drawable.book_caver_example,
    tags = listOf(
        R.string.tag_romantic,
        R.string.tag_scary,
        R.string.tag_idea,
        R.string.tag_dog,
        R.string.tag_funny,
        R.string.tag_life,
        R.string.tag_poem
    )
)

@Composable
fun BookDetailsScreen(book: Book, modifier: Modifier = Modifier )
{
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(book.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.25f
        )


        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp,0.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
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
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_language),
                        null,
                        //tint = Color.Unspecified,
                        )
                    Text(stringResource(book.language))
                }

                Image(
                    painter = painterResource(book.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        //.align(Alignment.CenterHorizontally)
                        .width(140.dp)
                        .height(210.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_document),
                        null,
                        //tint = Color.Unspecified,
                        )
                    Text(stringResource(book.pageNumber))
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(book.title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                )

            Text(
                text = stringResource(book.author),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
                )

            Spacer(Modifier.height(24.dp))


                Text(
                    stringResource(R.string.book_details_tags_title),
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    book.tags.forEach { tag ->

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF3F4F6),
                            shadowElevation = 0.dp
                        ) {
                            Text(
                                text = stringResource(tag),
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 4.dp
                                ),
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.book_details_description_title),
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(book.bookDescription),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.book_details_notes_title),
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(book.bookNotes),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(Modifier.height(24.dp))
        }
    }
}
