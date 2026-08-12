package com.example.pocketlibrary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketlibrary.data.local.entity.BookTagCrossRef

@Dao
interface BookTagDao {
    @Insert
    suspend fun insertAll(refs: List<BookTagCrossRef>)

    @Query("DELETE FROM book_tag_cross_ref WHERE bookId = :bookID")
    suspend fun deleteCrossRefsForBook(bookID: Long)
}