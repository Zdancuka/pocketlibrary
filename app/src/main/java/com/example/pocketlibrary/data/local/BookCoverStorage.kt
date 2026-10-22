package com.example.pocketlibrary.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import androidx.core.net.toUri

object BookCoverStorage {
    private const val DIR_NAME = "covers"
    private const val MAX_SIDE_PX = 400
    private const val MAX_BASE64_CHARS = 600_000
    private val QUALITIES = intArrayOf(75 , 55 , 40)

    fun isLocalUri(uri : String?) : Boolean =
        uri != null && (uri.startsWith("content://") || uri.startsWith("file://"))

    private fun coverDir(context : Context) : File =
        File(context.filesDir, DIR_NAME).apply { if (! exists()) mkdirs() }

    fun encodeToBase64(context : Context , uriString : String) : String? = try {
        val source = ImageDecoder.createSource(context.contentResolver, uriString.toUri())
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            val w = info.size.width
            val h = info.size.height
            val scale = min(1f, MAX_SIDE_PX.toFloat() / max(w , h))
            decoder.setTargetSize(
                (w * scale).roundToInt().coerceAtLeast(1) ,
                (h * scale).roundToInt().coerceAtLeast(1)
            )
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
        var result : String? = null
        for (quality in QUALITIES) {
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            val encoded = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
            if (encoded.length <= MAX_BASE64_CHARS) {
                result = encoded
                break
            }
        }

        bitmap.recycle()
        result
    } catch (e : Exception) {
        android.util.Log.e("BookCoverStorage", "encode failed", e)
        null
    }

    fun saveFromBase64(context : Context, bookId : String, base64 : String) : String? = try {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        deleteForBook(context, bookId)
        val file = File(coverDir(context) , "${bookId}_${System.currentTimeMillis()}.jpg")
        file.writeBytes(bytes)
        Uri.fromFile(file).toString()
    } catch (e : Exception) {
        android.util.Log.e("BookCoverStorage" , "save failed" , e)
        null
    }

    fun deleteForBook(context : Context, bookId : String) {
        coverDir(context)
            .listFiles { f -> f.name.startsWith("${bookId}_") }
            ?.forEach { it.delete() }
    }
}