package com.example.pocketlibrary

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pocketlibrary.data.local.database.PocketLibraryDatabase
import com.example.pocketlibrary.data.repository.BookRepository

class PocketLibraryApplication: Application() {

    lateinit var database: PocketLibraryDatabase
        private set

    lateinit var bookRepository: BookRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            PocketLibraryDatabase::class.java,
            "pocket_library.db"
        )
            // SQLite disables foreign key enforcement by default; enable it per connection
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.execSQL("PRAGMA foreign_keys = ON")
                }
            })
            .build()

        bookRepository = BookRepository(database)
    }
}