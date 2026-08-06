package com.example.pocketlibrary.data.repository

import androidx.room.withTransaction
import com.example.pocketlibrary.data.local.dao.BookDao
import com.example.pocketlibrary.data.local.database.PocketLibraryDatabase
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookTagCrossRef
import com.example.pocketlibrary.data.local.entity.TagEntity

class BookRepository(
    private val database: PocketLibraryDatabase
) {
    fun observeBookWithTags(bookId: Long)= database.bookDao().observeBookWithTags(bookId)
    fun observeBooksWithTags()= database.bookDao().observeBooksWithTags()

    suspend fun deleteBook(bookId: Long){
        database.bookDao().deleteBookAndRef(bookId)
    }

    suspend fun addBookWithTags(
        book: BookEntity,
        tags: List<String>
    ): Long = database.withTransaction {

        val bookId = database.bookDao().insertBook(book)

        val tagIds = tags
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .map { tagName ->
                val existing = database.tagDao().findByName(tagName)
                if (existing != null) {
                    existing.tagId
                } else {
                    val newId = database.tagDao().insert(TagEntity(name = tagName))
                    // `!!` can crash app; safer next step is handling null explicitly and returning a clear failure.
                    // Try to avoid !! usage where it is possible
                    if (newId != -1L) newId else database.tagDao().findByName(tagName)!!.tagId
                }
            }

        database.bookTagDao().insertAll(
            tagIds.map { tagId ->
                BookTagCrossRef(bookId = bookId, tagId = tagId)
            }
        )

        bookId

    }
}