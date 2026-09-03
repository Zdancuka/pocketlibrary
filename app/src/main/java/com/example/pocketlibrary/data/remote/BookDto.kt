package com.example.pocketlibrary.data.remote

data class BookDto (
    val bookId: String = "",
    val title: String,
    val author: String,
    val language: String? = null,
    val pageNumber: Int? = null,
    val bookDescription: String? = null,
    val bookNotes: String? = null,
    val imageUri: String? = null,
    val updatedAt: Long = 0L,
    val tags: List<String> = emptyList()
)