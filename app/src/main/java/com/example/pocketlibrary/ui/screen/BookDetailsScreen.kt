package com.example.pocketlibrary.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.data.local.entity.TagEntity
import com.example.pocketlibrary.navigation.Screen
import com.example.pocketlibrary.ui.screen.element.DeleteBookDialog
import com.example.pocketlibrary.ui.screen.element.OutlinedTag
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

@Composable
fun BookDetailsScreen(
    bookViewModel: BookViewModel,
    bookWithTags: BookWithTags,
    navController: NavHostController
) {
    val book = bookWithTags.book
    val tags = bookWithTags.tags

    var showDeleteDialog by remember { mutableStateOf(false) }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showDeleteDialog) {
        DeleteBookDialog(
            bookTitle = book.title,
            onConfirm = {
                showDeleteDialog = false
                bookViewModel.deleteBook(book.bookId)
                navController.popBackStack()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    if (isLandscape) {
        BookDetailsLandscape(
            book = book,
            tags = tags,
            navController = navController,
            onDeleteClick = { showDeleteDialog = true }
        )
    }
    else {
        BookDetailsPortrait(
            book = book,
            tags = tags,
            navController = navController,
            onDeleteClick = { showDeleteDialog = true }
        )
    }
}

@Composable
private fun BookDetailsPortrait(
    book: BookEntity,
    tags: List<TagEntity>,
    navController: NavHostController,
    onDeleteClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
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
                    onDeleteClick = onDeleteClick
                )
            }
        }

        item {
            BookDetailsContent(
                tags = tags,
                book = book,
                modifier = Modifier.padding(horizontal = Dimens.SpaceLarge)
            )
        }

    }
}

@Composable
private fun BookDetailsLandscape(
    book: BookEntity,
    tags: List<TagEntity>,
    navController: NavHostController,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(Dimens.BookCoverWidthMedium)
                .padding(Dimens.SpaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailsActionButtons(
                onBack = { navController.popBackStack() },
                onEditClick = { navController.navigate(Screen.Edit.createRoute(book.bookId)) },
                onDeleteClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

            AsyncImage(
                model = book.imageUri ?: R.drawable.book_caver_example,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .clip(RoundedCornerShape(Dimens.CornerSmall)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = book.author,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            item {
                BookDetailsContent(
                    tags = tags,
                    book = book,
                    modifier = Modifier
                        .padding(
                            horizontal = Dimens.SpaceLarge,
                            vertical = Dimens.SpaceMedium
                        )
                )
            }
        }
    }
}


@Composable
private fun BookDetailsContent(
    tags: List<TagEntity>,
    book: BookEntity,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.SpaceMedium)
    ) {

        Text(
            stringResource(R.string.tags),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

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

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

        Text(
            text = book.bookDescription ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        Text(
            text = stringResource(R.string.notes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

        Text(
            text = book.bookNotes ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
    }
}


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

        DetailsActionButtons(
            onBack = onBack,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceLarge)
        )

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

@Composable
private fun DetailsActionButtons(
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(Dimens.IconSize)
            )
        }

        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(Dimens.IconSize)
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(Dimens.IconSize)
            )
        }
    }
}

