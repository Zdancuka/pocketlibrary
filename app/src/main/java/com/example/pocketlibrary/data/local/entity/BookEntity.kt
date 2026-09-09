package com.example.pocketlibrary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity (tableName = "books")
data class BookEntity(
    @PrimaryKey ()
    val bookId: String = UUID.randomUUID().toString(),
    val uid : String = "",
    val title: String,
    val author: String,
    val language: String? = null,
    val pageNumber: Int? = null,
    val bookDescription: String? = null,
    val bookNotes: String? = null,
    val imageUri: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)