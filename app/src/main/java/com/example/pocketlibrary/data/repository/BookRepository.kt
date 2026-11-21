package com.example.pocketlibrary.data.repository

import android.content.Context
import androidx.room.withTransaction
import android.net.Uri
import com.example.pocketlibrary.data.local.BookTextStorage
import com.example.pocketlibrary.data.local.BookCoverStorage
import com.example.pocketlibrary.data.local.database.PocketLibraryDatabase
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.data.local.entity.BookTagCrossRef
import com.example.pocketlibrary.data.local.entity.TagEntity
import com.example.pocketlibrary.data.remote.BookDto
import com.example.pocketlibrary.data.remote.BookRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(
    private val database : PocketLibraryDatabase ,
    private val remoteDataSource : BookRemoteDataSource,
    private val appContext: Context
) {

    fun observeBookWithTags(
        uid : String ,
        bookId : String
    ) = database.bookDao().observeBookWithTags(uid , bookId)

    fun observeBooksWithTags(
        uid : String
    ) = database.bookDao().observeBooksWithTags(uid)

    suspend fun deleteBook(
        uid : String ,
        bookId : String
    ) {
        val local = database.bookDao().getBookOnce(bookId)
        database.bookDao().deleteBookAndRef(uid , bookId)
        local?.contentFileName?.let { fileName ->
            withContext(Dispatchers.IO){
                BookTextStorage.deleteText(appContext, fileName)
            }
        }
        withContext(Dispatchers.IO){
            BookCoverStorage.deleteForBook(appContext, bookId)
        }
        runCatching {
            remoteDataSource.markBookDeleted(
                uid , bookId , System.currentTimeMillis()
            )
        }.onFailure { e ->
            android.util.Log.e("BookRepository" , "markBook failed" , e)
        }
    }

    suspend fun addBookWithTags(
        uid : String ,
        book : BookEntity ,
        tags : List<String>
    ) {
        //val stamped = book.copy(uid = uid)
        lateinit var stamped : BookEntity

        database.withTransaction {
            val existing = database.bookDao().getBookOnce(book.bookId)
            stamped = book.copy(
                uid = uid,
                contentFileName = book.contentFileName ?: existing?.contentFileName
            )
            if(existing == null){
            database.bookDao().insertBook(stamped)
            } else{
                database.bookDao().updateBook(stamped)
            }

            val tagIds = resolveTagIds(tags)
            database.bookTagDao().insertAll(
                tagIds.map { tagId ->
                    BookTagCrossRef(bookId = stamped.bookId , tagId = tagId)
                }
            )
        }
        pushToRemote(uid , stamped , tags)
    }

    suspend fun updateBookWithTags(
        uid : String ,
        book : BookEntity ,
        tags : List<String>
    ) {

        val updated = book.copy(uid = uid , updatedAt = System.currentTimeMillis())

        database.withTransaction {
            database.bookDao().updateBook(updated)
            database.bookTagDao().deleteCrossRefsForBook(updated.bookId)

            val tagIds = resolveTagIds(tags)
            database.bookTagDao().insertAll(
                tagIds.map { tagId ->
                    BookTagCrossRef(
                        bookId = updated.bookId ,
                        tagId = tagId
                    )
                }
            )
        }
        pushToRemote(uid , updated , tags)
    }

    suspend fun attachBookText(
        uid: String,
        book: BookEntity,
        uri: Uri
    ): BookEntity? = withContext(Dispatchers.IO){
        val oldFileName = book.contentFileName
        val newFileName = BookTextStorage.saveText(appContext, book.bookId, uri)?: return@withContext null

        if (oldFileName != null && oldFileName != newFileName) {
            BookTextStorage.deleteText(appContext, oldFileName)
        }

        val updated = book.copy(
            uid = uid ,
            contentFileName = newFileName,
            updatedAt = System.currentTimeMillis()
        )

        val existing = database.bookDao().getBookOnce(updated.bookId)
        if(existing == null){
            database.bookDao().insertBook(updated)
        } else{
            database.bookDao().updateBook(updated)
        }
        updated
    }

    suspend fun removeBookText(book : BookEntity) {
        withContext(Dispatchers.IO) {
            BookTextStorage.deleteText(appContext , book.contentFileName)
        }
        val updated = book.copy(contentFileName = null , updatedAt = System.currentTimeMillis())
        database.bookDao().updateBook(updated)
    }

    suspend fun readBookText(book : BookEntity) : String? = withContext(Dispatchers.IO) {
        val fileName = book.contentFileName ?: return@withContext null
        BookTextStorage.readText(appContext , fileName)
    }

    suspend fun discardDraftBook(uid: String, bookId : String){
        val local = database.bookDao().getBookOnce(bookId) ?: return
        withContext(Dispatchers.IO){
            BookTextStorage.deleteText(appContext, local.contentFileName)
        }
        database.bookDao().deleteBookAndRef(uid, bookId)
    }

    suspend fun syncFromRemote(uid : String) {
        val remoteBooks = runCatching {
            remoteDataSource.fetchAllBooks(uid)
        }.onFailure { e-> android.util.Log.e(
            "BookRepository",
            "syncToRemote failed" ,
            e
        ) } .getOrNull() ?: return

        for (dto in remoteBooks) {
            val local = database.bookDao().getBookOnce(dto.bookId)

            if (dto.deleted) {
                if (local != null) {
                    withContext(Dispatchers.IO) {
                        local.contentFileName?.let {
                            BookTextStorage.deleteText(appContext, it)
                        }
                        BookCoverStorage.deleteForBook(appContext, dto.bookId)
                    }
                    database.bookDao().deleteBookAndRef(uid , dto.bookId)
                }
                continue
            }

            if (local == null || dto.updatedAt > local.updatedAt) {

                val imageUri = dto.coverBase64?.let { base64 ->
                    withContext(Dispatchers.IO){
                        BookCoverStorage.saveFromBase64(appContext, dto.bookId, base64)
                    }
                } ?: dto.imageUri

                val entity = BookEntity(
                    bookId = dto.bookId ,
                    uid = uid ,
                    title = dto.title ,
                    author = dto.author ,
                    language = dto.language ,
                    pageNumber = dto.pageNumber ,
                    bookDescription = dto.bookDescription ,
                    bookNotes = dto.bookNotes ,
                    imageUri = imageUri ,
                    updatedAt = dto.updatedAt,
                    contentFileName = local?.contentFileName
                )
                database.withTransaction {
                    if (local == null) database.bookDao().insertBook(entity)
                    else database.bookDao().updateBook(entity)

                    database.bookTagDao().deleteCrossRefsForBook(entity.bookId)
                    val tagIds = resolveTagIds(dto.tags)
                    database.bookTagDao().insertAll(
                        tagIds.map { tagId ->
                            BookTagCrossRef(
                                bookId = entity.bookId ,
                                tagId = tagId
                            )
                        }
                    )
                }
            }
        }
    }

    private suspend fun pushToRemote(uid : String , book : BookEntity , tag : List<String>) {

        val hasLocalCover = BookCoverStorage.isLocalUri(book.imageUri)
        val coverBase64 = if (hasLocalCover){
            withContext(Dispatchers.IO){
                BookCoverStorage.encodeToBase64(appContext, book.imageUri!!)
            }
        } else null

        runCatching {
            remoteDataSource.pushBook(
                uid ,
                BookDto(
                    bookId = book.bookId ,
                    title = book.title ,
                    author = book.author ,
                    language = book.language ,
                    pageNumber = book.pageNumber ,
                    bookDescription = book.bookDescription ,
                    bookNotes = book.bookNotes ,
                    imageUri = if (hasLocalCover) null else book.imageUri ,
                    updatedAt = book.updatedAt ,
                    tags = tag,
                    coverBase64 = coverBase64
                )
            )
        }.onFailure { e -> android.util.Log.e(
            "BookRepository" ,
            "pushToRemote failed" ,
            e)
        }
    }

    private suspend fun resolveTagIds(tags : List<String>) : List<Long> =
        tags
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .map { tagName ->
                val existing = database.tagDao().findByName(tagName)
                if (existing != null) {
                    existing.tagId
                } else {
                    val newId = database.tagDao().insert(TagEntity(name = tagName))
                    if (newId != - 1L) newId else database.tagDao().findByName(tagName) !!.tagId
                }

            }
}