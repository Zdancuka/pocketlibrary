package com.example.pocketlibrary.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketlibrary.data.local.dao.BookDao
import com.example.pocketlibrary.data.local.dao.BookTagDao
import com.example.pocketlibrary.data.local.dao.TagDao
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookTagCrossRef
import com.example.pocketlibrary.data.local.entity.TagEntity

@Database(
    entities = [BookEntity::class, TagEntity::class, BookTagCrossRef::class
               ],
    version = 7,
    exportSchema = false
)
abstract class PocketLibraryDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun tagDao(): TagDao
    abstract fun bookTagDao(): BookTagDao
}