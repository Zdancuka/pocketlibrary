package com.example.pocketlibrary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.pocketlibrary.data.local.entity.BookTagCrossRef

@Dao
interface BookTagDao {
    @Insert
    suspend fun insertAll(refs: List<BookTagCrossRef>)
}