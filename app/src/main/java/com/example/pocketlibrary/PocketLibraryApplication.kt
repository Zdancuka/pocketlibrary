package com.example.pocketlibrary

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.example.pocketlibrary.data.local.database.PocketLibraryDatabase
import com.example.pocketlibrary.data.remote.BookRemoteDataSource
import com.example.pocketlibrary.data.repository.BookRepository

class PocketLibraryApplication: Application(), SingletonImageLoader.Factory {

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
            .fallbackToDestructiveMigration(dropAllTables = true)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.execSQL("PRAGMA foreign_keys = ON")
                }
            })
            .build()

        bookRepository = BookRepository(
            appContext = applicationContext,
            database = database,
            remoteDataSource = BookRemoteDataSource()
        )
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader{
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory())
            }
            .build()
    }
}