package com.example.pocketlibrary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "books")
data class BookEntity(
    @PrimaryKey (autoGenerate = true)
    val bookId: Long = 0,
    val title: String,
    val author: String,
    val language: String? = null,
    val pageNumber: Int? = null,
    val bookDescription: String? = null,
    val bookNotes: String? = null,
    val imageUri: String? = null,
)