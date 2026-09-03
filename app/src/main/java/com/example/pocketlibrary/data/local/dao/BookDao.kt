package com.example.pocketlibrary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Transaction
    @Query("SELECT * FROM books WHERE uid = :uid ORDER BY bookId DESC")
    fun observeBooksWithTags(uid: String): Flow<List<BookWithTags>>

    @Transaction
    @Query("SELECT * FROM books WHERE uid = :uid AND bookId = :bookId LIMIT 1")
    fun observeBookWithTags(uid: String, bookId: String): Flow<BookWithTags?>

    @Transaction
    @Query ("SELECT * FROM books WHERE uid = :uid")
    fun getAllBookOnce (uid: String): List<BookWithTags>

    @Transaction
    @Query("SELECT * FROM books WHERE bookId = :bookId LIMIT 1")
    fun getBookOnce(bookId: String): BookEntity?


    // CASCADE on BookTagCrossRef handles cross-ref cleanup automatically
    @Query("DELETE FROM books WHERE uid = :uid AND bookId = :bookId")
    suspend fun deleteBookAndRef( uid: String, bookId: String)
}