package com.example.pocketlibrary

import android.app.Application
import androidx.room.Room
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
        ).build()

        bookRepository = BookRepository(database)
    }
}