package com.example.pocketlibrary.data.remote.openlibrary

class OpenLibraryRemoteDataSource (
    private val api: OpenLibraryApiService = OpenLibraryClint.api
){
    suspend fun searchBooks(query: String): List<OpenLibraryDoc> {
        if (query.isBlank()) return emptyList()
        return runCatching {
            api.searchBooks(query).docs
        }.onFailure { e ->
            android.util.Log.e("OpenLibraryRemoteDataSource", "search failed", e)
        }.getOrDefault(emptyList())
    }
}