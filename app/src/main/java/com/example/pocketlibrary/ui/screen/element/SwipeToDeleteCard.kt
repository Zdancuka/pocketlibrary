package com.example.pocketlibrary.ui.screen.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

// Right now this component receives the whole BookViewModel and BookWithTags,
// but it only needs two things: the book title (to show in the dialog) and
// something to call when the user confirms delete.
//
// The cleaner pattern is to pass only a lambda (a simple function):
//
// BETTER SIGNATURE:
// fun SwipeToDeleteCard(
//     bookTitle: String,       // only the title, not the whole BookWithTags
//     onDelete: () -> Unit,    // just "do the delete", the caller decides how
//     content: @Composable () -> Unit
// )
//
// Then the caller (e.g. LibraryScreen) passes the lambda like this:
// SwipeToDeleteCard(
//     bookTitle = bookWithTags.book.title,
//     onDelete = { bookViewModel.deleteBook(bookWithTags.book.bookId) }
// ) { BookCard(...) }
//
// This way SwipeToDeleteCard doesn't know or care about ViewModels at all.
// Same improvement was applied to DetailsTop earlier — same idea here.
@OptIn (ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteCard(
    bookWithTags: BookWithTags,
    bookViewModel: BookViewModel,
    content: @Composable () -> Unit
){
    var showDeleteDialog by remember { mutableStateOf(false) }

    val dismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart){
                showDeleteDialog = true
            }
            false
        }
    )

    if (showDeleteDialog) {
        DeleteBookDialog(
            bookTitle = bookWithTags.book.title,
            onConfirm = {
                showDeleteDialog = false
                bookViewModel.deleteBook(bookWithTags.book.bookId)
            },
            onDismiss = { showDeleteDialog = false}
        )
    }

    SwipeToDismissBox(
        state = dismissBoxState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(Dimens.CornerXLarge))
                    .background(MaterialTheme.colorScheme.error)
                    .padding(horizontal = Dimens.SpaceMedium),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete) ,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) {
        content()
    }
}