package com.example.pocketlibrary.ui.screen.reader

import androidx.compose.runtime.Composable
import com.example.pocketlibrary.data.local.BookTextStorage
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

@Composable
fun ReaderScreen(
    book: BookEntity,
    bookViewModel: BookViewModel,
    onBack: () -> Unit
){
    if (BookTextStorage.isPdf(book.contentFileName)){
        PdfReaderScreen(book= book, onBack = onBack)
    } else{
        TextReaderScreen(book = book, bookViewModel = bookViewModel, onBack = onBack)
    }
}
