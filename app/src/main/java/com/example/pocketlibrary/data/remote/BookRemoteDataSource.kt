package com.example.pocketlibrary.data.remote

import com.example.pocketlibrary.data.local.entity.BookEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun booksCollection(uid: String) =
        firestore.collection("users")
            .document(uid)
            .collection("books")

    suspend fun pushBook(uid: String, book: BookDto){
        booksCollection(uid)
            .document(book.bookId)
            .set(book).await()
    }


    suspend fun deleteBook(uid: String, bookId: String){
        booksCollection(uid)
            .document(bookId)
            .delete().await()
    }

    suspend fun fetchAllBooks(uid: String): List<BookDto>{
        val snapshot = booksCollection(uid).get().await()
        return snapshot.documents.mapNotNull { it.toObject(BookDto::class.java) }
    }
}