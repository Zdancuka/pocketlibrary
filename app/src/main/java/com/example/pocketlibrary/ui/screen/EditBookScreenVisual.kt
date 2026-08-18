package com.example.pocketlibrary.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

@Composable
fun EditBookScreenVisual(
    bookWithTags: BookWithTags,
    bookViewModel: BookViewModel,
    onBookUpdate: () -> Unit = {}
) {
    BookFormScreen(
        bookWithTags = bookWithTags,
        screenTitle = stringResource(R.string.edit_book),
        saveButtonText = stringResource(R.string.edit),
        onSave = { book, tags ->
            bookViewModel.updateBook(book, tags)
            onBookUpdate()
        }
    )
}
