package com.example.pocketlibrary.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.pocketlibrary.data.local.entity.BookEntity

// CASCADE ensures cross-ref rows are removed automatically when a book is deleted
@Entity(
    tableName = "book_tag_cross_ref",
    primaryKeys = ["bookId", "tagId"],
    foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = ["bookId"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BookTagCrossRef(
    val bookId: Long,
    val tagId: Long
)