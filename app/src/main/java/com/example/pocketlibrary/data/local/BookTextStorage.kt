package com.example.pocketlibrary.data.local

import android.content.Context
import java.io.File
import android.net.Uri

object BookTextStorage {
    private const val DIR_NAME = "book_text"
    const val MAX_TEXT_BYTES = 5L * 1024 * 1024 //5MB for .txt
    const val MAX_PDF_BYTES = 25L * 1024 * 1024 //25MB for .pdf

    private fun textDir(context : Context) : File =
        File(context.filesDir , DIR_NAME).apply { if (! exists()) mkdirs() }

    fun isPdf(fileName: String?): Boolean =
        fileName?.endsWith(".pdf", ignoreCase = true) == true

    fun getFile(context: Context, fileName: String): File =
        File(textDir(context), fileName)

    private fun looksLikePDf(file: File): Boolean = try{
        val head = ByteArray(1024)
        val read = file.inputStream().use { it.read(head)}
        read > 0 && String(head, 0, read, Charsets.ISO_8859_1).contains("%PDF-")
    }catch (e: Exception){
        false
    }

    fun saveText(context : Context , bookId : String , uri : Uri) : String? {
        val dir = textDir(context)
        val tmp = File(dir, "$bookId.tmp")

        return try {
            val copied = context.contentResolver.openInputStream(uri)?.use { input ->
                tmp.outputStream().use { output ->
                    val buffer = ByteArray(8 * 1024)
                    var totalRead = 0L
                    var read : Int
                    while (input.read(buffer).also { read = it } != - 1) {
                        totalRead += read
                        if (totalRead > MAX_TEXT_BYTES) {
                            return@use false
                        }
                        output.write(buffer , 0 , read)
                    }
                    true
                }
            } ?: false

            if (! copied) {
                tmp.delete()
                return null
            } else{}

            val pdf = looksLikePDf(tmp)
            if (!pdf && tmp.length()> MAX_TEXT_BYTES){
                tmp.delete()
                return null
            }

            val fileName = if (pdf) "$bookId.pdf" else "$bookId.txt"
            val dest = File (dir, fileName)
            dest.delete()

            if(!tmp.renameTo(dest)){
                tmp.delete()
                return null
            }

            fileName
        } catch (_ : Exception) {
            tmp.delete()
            null
        }
    }

    fun readText(context : Context , fileName : String) : String? {
        val file = File(textDir(context) , fileName)
        return if (file.exists()) {
            try {
                file.readText()
            } catch (_ : Exception) {
                null
            }
        } else null
    }

    fun deleteText(context : Context , fileName : String?) {
        if (fileName == null) return
        File(textDir(context) , fileName).delete()
    }
}
