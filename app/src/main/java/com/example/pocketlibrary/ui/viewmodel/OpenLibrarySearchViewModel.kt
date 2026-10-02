package com.example.pocketlibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketlibrary.data.remote.openlibrary.OpenLibraryDoc
import com.example.pocketlibrary.data.remote.openlibrary.OpenLibraryRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

const val SEARCH_DELAY:Int = 400

class OpenLibrarySearchViewModel(
    private val remoteDataSource: OpenLibraryRemoteDataSource = OpenLibraryRemoteDataSource()
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<OpenLibraryDoc>>(emptyList())
    val results: StateFlow<List<OpenLibraryDoc>> = _results

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String){
        _query.value = newQuery
        searchJob?.cancel()

        if(newQuery.isBlank()){
            _results.value=emptyList()
            _isSearching.value=false
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY.milliseconds)
            _isSearching.value=true
            _results.value= remoteDataSource.searchBooks(newQuery)
            _isSearching.value=false
        }
    }

    fun clearResult(){
        searchJob?.cancel()
        _results.value = emptyList()
        _query.value = ""
        _isSearching.value = false
    }
}