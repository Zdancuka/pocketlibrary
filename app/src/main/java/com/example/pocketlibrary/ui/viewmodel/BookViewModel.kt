package com.example.pocketlibrary.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.data.repository.BookRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val STOP_TIMEOUT_MS = 5000L

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModel (
    private val repository: BookRepository
): ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val currentUid = MutableStateFlow(auth.currentUser?.uid)

    private val authListener = FirebaseAuth.AuthStateListener {
        firebaseAuth ->
        currentUid.value = firebaseAuth.currentUser?.uid
    }

    init {
        auth.addAuthStateListener (authListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener (authListener)
    }

    private val uid: String?
        get() = currentUid.value

    val books: StateFlow<List<BookWithTags>> = currentUid
        .flatMapLatest { uid ->
            if (uid == null) {
                flowOf(emptyList())
            }
            else {
                repository.observeBooksWithTags(uid)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList()
        )

    val searchQuery = MutableStateFlow("")

    val filters = MutableStateFlow(BookFilters())

    val availableTags: StateFlow<List<String>> = books
        .map { list ->
            list.flatMap { it.tags.map { tag -> tag.name } }
                .distinctBy { it.lowercase() }
                .sortedBy { it.lowercase() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList()
        )

    val filteredBooks: StateFlow<List<BookWithTags>> = combine(
        books,
        searchQuery,
        filters
    ) { books, query, filters ->
        books.applySearchAndFilters(query, filters)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = emptyList()
    )

    fun toggleTag(tag: String) {
        filters.update { it.copy(selectedTags = it.selectedTags.toggled(tag)) }
    }

    fun setOnlyWithFile(enabled: Boolean) {
        filters.update { it.copy(onlyWithFile = enabled) }
    }

    fun setSort(sort: BookSort) {
        filters.update { it.copy(sort = sort) }
    }

    fun clearFilters() {
        filters.update {
            it.copy(selectedTags = emptySet(), onlyWithFile = false)
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun bookFlow(bookId: String): Flow<BookWithTags?> {
        val currentUid = uid ?: return flowOf(null)
        return repository.observeBookWithTags(currentUid, bookId)
    }

    fun addBook(book: BookEntity, tags: List<String>){
        val currentUid = uid ?: return
        viewModelScope.launch {
            repository.addBookWithTags(currentUid, book, tags)
            }
        }


    fun updateBook(book: BookEntity, tags: List<String>){
        val currentUid = uid ?: return
        viewModelScope.launch {
            repository.updateBookWithTags(currentUid, book, tags)
        }
    }

    fun deleteBook(bookId: String){
        val currentUid = uid ?: return
        viewModelScope.launch {
            try{
                repository.deleteBook(currentUid, bookId)
            } catch ( e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun syncFromRemote() {
        val currentUid = uid ?: return
        viewModelScope.launch {
            repository.syncFromRemote(currentUid)
        }
    }

    fun attachBookText(book: BookEntity, uri: Uri, onResult: (BookEntity?) -> Unit = {}){
        val currentUid = uid ?: return
        viewModelScope.launch {
            val updated = repository.attachBookText(currentUid, book, uri)
            onResult(updated)
        }
    }

    fun removeBookText(book: BookEntity, onDone: () -> Unit = {} ){
        viewModelScope.launch {
            repository.removeBookText(book)
            onDone()
        }
    }

    fun discardDraftBook(bookId: String){
        val currentUid = uid ?: return
        viewModelScope.launch { repository.discardDraftBook(currentUid, bookId) }
    }

    suspend fun readBookText(book: BookEntity): String? = repository.readBookText(book)

    class Factory(private val repository: BookRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress ("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
    }
}

private fun Set<String>.toggled(value: String): Set<String> =
        if (value in this) this - value else this + value