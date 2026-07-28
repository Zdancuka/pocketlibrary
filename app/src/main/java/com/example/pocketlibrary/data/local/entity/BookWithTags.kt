package com.example.pocketlibrary.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BookWithTags(
    @Embedded val book: BookEntity,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "tagId",
        associateBy = Junction(BookTagCrossRef::class)
    )
    val tags: List<TagEntity>
)