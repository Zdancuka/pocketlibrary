package com.example.pocketlibrary.ui.viewmodel

import com.example.pocketlibrary.data.local.entity.BookWithTags

enum class BookSort { RECENT , TITLE , AUTHOR }

data class BookFilters(
    val selectedTags : Set<String> = emptySet(),
    val onlyWithFile : Boolean = false,
    val sort : BookSort = BookSort.RECENT
) {
    val activeCount : Int
        get() = selectedTags.size + (if (onlyWithFile) 1 else 0)
}

fun List<BookWithTags>.applySearchAndFilters(
    query : String,
    filters : BookFilters
) : List<BookWithTags> {
    val words = query.trim().lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
    val wantedTags = filters.selectedTags.map { it.lowercase() }

    val filtered = filter { item ->
        val book = item.book
        val tagNames = item.tags.map { it.name.lowercase() }

        val matchesQuery = words.isEmpty() || run {
            val haystack = buildString {
                append(book.title).append(' ')
                append(book.author).append(' ')
                append(book.bookDescription.orEmpty()).append(' ')
                tagNames.forEach { append(it).append(' ') }
            }.lowercase()
            words.all { it in haystack }
        }

        matchesQuery &&
                wantedTags.all { it in tagNames } &&
                (! filters.onlyWithFile || book.contentFileName != null)
    }

    return when (filters.sort) {
        BookSort.RECENT -> filtered.sortedByDescending { it.book.updatedAt }
        BookSort.TITLE -> filtered.sortedWith(
            compareBy(String.CASE_INSENSITIVE_ORDER) { it.book.title }
        )

        BookSort.AUTHOR -> filtered.sortedWith(
            compareBy(String.CASE_INSENSITIVE_ORDER) { it.book.author }
        )
    }
}