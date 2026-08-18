package com.example.pocketlibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.data.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    // Holds the current text typed in the search bar.
    // Kept in the ViewModel so the query survives recomposition and screen rotation.
    val searchQuery = MutableStateFlow("")

    // combine merges two flows into one: whenever books or searchQuery changes,
    // this recalculates and emits a new filtered list — no manual filtering in the UI.
    val filteredBooks: StateFlow<List<BookWithTags>> = combine(books, searchQuery) { books, query ->
        if (query.isBlank()) {
            books
        } else {
            books.filter { bookWithTags ->
                bookWithTags.book.title.contains(query, ignoreCase = true) ||
                    bookWithTags.book.author.contains(query, ignoreCase = true) ||
                    bookWithTags.tags.any { tag -> tag.name.contains(query, ignoreCase = true) }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun bookFlow(bookId: Long): Flow<BookWithTags?> =
        repository.observeBookWithTags(bookId)

    fun addBook(book: BookEntity, tags: List<String>){
        viewModelScope.launch {
            repository.addBookWithTags(book, tags)
        }
    }

    fun deleteBook(bookId: Long){
        viewModelScope.launch {
            try{
                repository.deleteBook(bookId)
            } catch ( e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //better to wrap with try catch and handle exception in case of failure same as in delete
    fun updateBook(book: BookEntity, tags: List<String>){
        viewModelScope.launch {
            repository.updateBookWithTags(book, tags)
        }
    }

    class Factory(private val repository: BookRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress ("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
    }
}