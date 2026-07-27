package com.example.pocketlibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.data.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookViewModel (
    private val repository: BookRepository
): ViewModel(){

    val books: StateFlow<List<BookWithTags>> = repository
        .observeBooksWithTags()
        .stateIn(
            scope = viewModelScope,
            // Magic number, better move timeout to a named const.
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun bookFlow(bookId: Long): Flow<BookWithTags?> =
        repository.observeBookWithTags(bookId)

    fun addBook(book: BookEntity, tags: List<String>){
        viewModelScope.launch {
            repository.addBookWithTags(book, tags)
        }
    }

    class Factory(private val repository: BookRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress ("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
    }
}