package com.example.pocketlibrary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: BookEntity): Long

    @Transaction
    @Query("SELECT * FROM books ORDER BY title")
    fun observeBooksWithTags(): Flow<List<BookWithTags>>

    @Transaction
    @Query("SELECT * FROM books WHERE bookId = :bookId LIMIT 1")
    fun observeBookWithTags(bookId:Long): Flow<BookWithTags?>

    // CASCADE on BookTagCrossRef handles cross-ref cleanup automatically
    @Query("DELETE FROM books WHERE bookId = :bookId")
    suspend fun deleteBookAndRef(bookId: Long)
}