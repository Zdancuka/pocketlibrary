package com.example.pocketlibrary.data.remote.openlibrary

import com.google.gson.annotations.SerializedName

data class OpenLibrarySearchResponse (
    val numFound: Int = 0,
    val docs: List<OpenLibraryDoc> = emptyList()
)

data class OpenLibraryDoc (
    val key: String = "",
    val title: String = "",
    @SerializedName("author_name") val authorName: List<String>? = null,
    @SerializedName("cover_i") val coverId: Long? = null,
    )

fun OpenLibraryDoc.coverUrl(size: String = "M"): String? =
    coverId?.let {"https://covers.openlibrary.org/b/id/$it-$size.jpg?default=false"}

fun OpenLibraryDoc.authorDisplay(): String =
    authorName?.joinToString (",") ?: ""