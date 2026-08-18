package com.example.pocketlibrary.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.viewmodel.BookViewModel


@Composable
fun AddBookScreenVisual(
    bookViewModel: BookViewModel,
    onBookSaved: () -> Unit = {}
) {
   BookFormScreen(
       bookWithTags = null,
       screenTitle = stringResource(R.string.add_a_book),
       saveButtonText = stringResource(R.string.save),
       onSave = { book, tags ->
           bookViewModel.addBook(book, tags)
           onBookSaved()
       }
   )
}