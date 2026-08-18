package com.example.pocketlibrary.ui.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.navigation.Screen
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

@Composable
fun BookDetailsScreen(
    bookViewModel: BookViewModel,
    bookWithTags: BookWithTags,
    navController: NavHostController) {
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            AsyncImage(
                model = book.imageUri ?: R.drawable.book_caver_example,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.DetailsHeaderImageHeight)
                    .blur(
                        radiusX = Dimens.BlurRadius,
                        radiusY = Dimens.BlurRadius,
                        edgeTreatment = BlurredEdgeTreatment(
                            RoundedCornerShape(0)
                        )
                    ),
                contentScale = ContentScale.Crop,
                alpha = 0.25f
            )


            DetailsTop(
                book = book,
                onBack = { navController.popBackStack() },
                onEditClick = { navController.navigate(Screen.Edit.createRoute(book.bookId)) },
                onDeleteClick = { showDeleteDialog = true }
            )
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.SpaceLarge),
            contentPadding = PaddingValues(Dimens.SpaceMedium)
        ) {

            item {
                Text(
                    stringResource(R.string.tags),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                FlowRow(
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
            }

            item {
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                Text(
                    text = book.bookDescription ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                    )
            }

            item {
                Text(
                    text = stringResource(R.string.notes),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                Text(
                    text = book.bookNotes ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// DetailsTop receives lambdas instead of NavHostController so it stays navigation-agnostic:
// it does not need to know how navigation works, only that something should happen on click.
// This also makes it possible to @Preview this composable without a real NavController.
@Composable
fun DetailsTop(
    book: BookEntity,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpaceLarge)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceLarge),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(Dimens.IconEditSize)
                )
            }

            IconButton(onClick = onDeleteClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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

            AsyncImage(
                model = book.imageUri ?: R.drawable.book_caver_example,
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

    }
}

