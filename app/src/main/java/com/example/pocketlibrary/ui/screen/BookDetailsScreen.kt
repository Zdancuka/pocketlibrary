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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

@Composable
fun BookDetailsScreen(
    bookViewModel: BookViewModel,
    bookWithTags: BookWithTags,
    navController: NavHostController)
{
    val book = bookWithTags.book
    val tags = bookWithTags.tags

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_book_title)) },
            text = { Text(stringResource(R.string.delete_book_message, book.title)) },
            confirmButton = {
                TextButton (
                    onClick = {
                        showDeleteDialog = false
                        bookViewModel.deleteBook(book.bookId)
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

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

                IconButton(
                    onClick = { navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = {
                    showDeleteDialog = true
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
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


            FlowRow (
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
            ) {

                tags.forEach { tag ->
                    OutlinedTag(tag.name)
                }

                Text(
                    text = stringResource(R.string.show_more),
                    modifier = Modifier.padding(top = Dimens.SpaceXSmall),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = TextDecoration.Underline
                )
            }



                Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

                Text(
                    text = stringResource(R.string.book_details_description_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

                Text(
                    text = book.bookDescription ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))

                Text(
                    text = stringResource(R.string.book_details_notes_title),
                    style = MaterialTheme.typography.titleMedium,
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
