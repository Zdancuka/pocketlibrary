package com.example.pocketlibrary.data.remote.openlibrary

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val LIMIT_BOOK_TO_SHOW_IN_SEARCH: Int =  20

interface OpenLibraryApiService {
    @GET("search.json")
    suspend fun searchBooks(
        @Query(value = "q") query : String ,
        @Query(value = "limit") limit : Int = LIMIT_BOOK_TO_SHOW_IN_SEARCH
    ) : OpenLibrarySearchResponse
}

object OpenLibraryClint {
    val api : OpenLibraryApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenLibraryApiService::class.java)
    }
}