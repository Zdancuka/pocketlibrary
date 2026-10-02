package com.example.pocketlibrary.data.local

import android.content.Context
import java.io.File
import android.net.Uri

object BookTextStorage {
    private const val DIR_NAME = "book_text"
    const val MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024 //5MB

    private fun textDir(context: Context): File =
        File(context.filesDir, DIR_NAME).apply {if (!exists()) mkdirs()}

        fun saveText(context: Context, bookId: String, uri: Uri): String? {
            val fileName = "$bookId.txt"
            val destFile = File(textDir(context), fileName)

            return try {
                val opened = context.contentResolver.openInputStream(uri)?.use { input ->
                    destFile.outputStream().use { output ->
                        val buffer = ByteArray(8 * 1024)
                        var totalRead = 0L
                        var read: Int
                        while (input.read(buffer).also { read = it } != -1) {
                            totalRead += read
                            if (totalRead > MAX_FILE_SIZE_BYTES) {
                                return@use false
                            }
                            output.write(buffer, 0, read)
                        }
                        true
                    }
                } ?: false

                if (!opened) {
                    destFile.delete()
                    null
                } else {
                    fileName
                }
            } catch (_: Exception) {
                destFile.delete()
                null
            }
        }

        fun readText(context: Context, fileName: String): String? {
            val file = File(textDir(context), fileName)
            return if (file.exists()) {
                try {
                    file.readText()
                } catch (_: Exception) {
                    null
                }
            } else null
        }

        fun deleteText(context: Context, fileName: String?) {
            if (fileName == null) return
            File(textDir(context), fileName).delete()
        }
    }
