package com.example.pocketlibrary.data.repository

import androidx.compose.ui.text.resolveDefaults
import androidx.room.withTransaction
import com.example.pocketlibrary.data.local.dao.BookDao
import com.example.pocketlibrary.data.local.database.PocketLibraryDatabase
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookTagCrossRef
import com.example.pocketlibrary.data.local.entity.TagEntity
import com.example.pocketlibrary.data.remote.BookDto
import com.example.pocketlibrary.data.remote.BookRemoteDataSource
import kotlin.String

class BookRepository(
    private val database: PocketLibraryDatabase,
    private val remoteDataSource: BookRemoteDataSource
) {

    fun observeBookWithTags(
        uid: String,
        bookId: String
    ) = database.bookDao().observeBookWithTags(uid, bookId)
    fun observeBooksWithTags(
        uid: String
    ) = database.bookDao().observeBooksWithTags(uid)

    suspend fun deleteBook(uid: String, bookId: String){
        database.bookDao().deleteBookAndRef(uid, bookId)
    }

    suspend fun addBookWithTags(
        uid: String,
        book: BookEntity,
        tags: List<String>
    ) {
        val stamped = book.copy(uid = uid)

        database.withTransaction {
            database.bookDao().insertBook(stamped)
            val tagIds = resolveTagIds(tags)

            database.bookTagDao().insertAll(
                tagIds.map { tagId ->
                    BookTagCrossRef(bookId = stamped.bookId, tagId = tagId)
                }
            )
        }
        pushToRemote(uid, stamped, tags)
    }

    suspend fun updateBookWithTags(
        uid: String,
        book: BookEntity,
        tags: List<String>
    ) {

        val updated = book.copy(uid= uid, updatedAt = System.currentTimeMillis())

        database.withTransaction {
            database.bookDao().updateBook(updated)
            database.bookTagDao().deleteCrossRefsForBook(updated.bookId)

            val tagIds = resolveTagIds(tags)
            database.bookTagDao().insertAll (
                tagIds.map {tagId -> BookTagCrossRef(
                bookId = updated.bookId,
                tagId = tagId) }
            )
        }
        pushToRemote(uid, updated, tags)
    }

    suspend fun syncFromRemote(uid: String){
        val remoteBooks = runCatching { remoteDataSource.fetchAllBooks(uid) }.getOrNull() ?: return

        for (dto in remoteBooks){
            val local = database.bookDao().getBookOnce(dto.bookId)
            if (local == null || dto.updatedAt > local.updatedAt){
                val entity = BookEntity(
                    bookId = dto.bookId,
                    uid = uid,
                    title = dto.title,
                    author = dto.author,
                    language = dto.language,
                    pageNumber= dto.pageNumber,
                    bookDescription = dto.bookDescription,
                    bookNotes = dto.bookNotes,
                    imageUri = dto.imageUri,
                    updatedAt = dto.updatedAt
                )
                database.withTransaction {
                    if (local == null) database.bookDao().insertBook(entity)
                    else database.bookDao().updateBook(entity)

                    database.bookTagDao().deleteCrossRefsForBook(entity.bookId)
                    val tagIds = resolveTagIds(dto.tags)
                    database.bookTagDao().insertAll(
                        tagIds.map {
                            tagId -> BookTagCrossRef(
                            bookId = entity.bookId,
                            tagId = tagId
                            )
                        }
                    )
                }
            }
        }
    }

    private suspend fun pushToRemote(uid: String,book: BookEntity, tag: List<String>){
        runCatching {
            remoteDataSource.pushBook(
                uid,
                BookDto(
                    bookId = book.bookId,
                    title = book.title,
                    author = book.author,
                    language = book.language,
                    pageNumber= book.pageNumber,
                    bookDescription = book.bookDescription,
                    bookNotes = book.bookNotes,
                    imageUri = book.imageUri,
                    updatedAt = book.updatedAt,
                    tags = tag
                )
            )
        }.onFailure { e -> android.util.Log.e("BookRepository", "pushToRemote failed", e) }
    }

    private suspend fun resolveTagIds(tags: List<String>): List<Long> =
        tags
            .map {it.trim()}
            .filter { it.isNotBlank() }
            .distinct()
            .map { tagName ->
                val existing = database.tagDao().findByName(tagName)
                if (existing != null){
                    existing.tagId
                } else {
                    val newId = database.tagDao().insert(TagEntity(name= tagName))
                    if (newId != -1L) newId else database.tagDao().findByName(tagName)!!.tagId
                }

            }
}