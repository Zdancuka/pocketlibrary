package com.example.pocketlibrary.data.local

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import coil3.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt

class PdfPageRenderer(file : File) {
    private val descriptor = ParcelFileDescriptor.open(file , ParcelFileDescriptor.MODE_READ_ONLY)
    private val renderer = try {
        PdfRenderer(descriptor)
    } catch (e : Exception) {
        descriptor.close()
        throw e
    }

    private val mutex = Mutex()
    private var closed = false

    val pageCount : Int = renderer.pageCount

    suspend fun renderPage(index : Int , targedWidthPx : Int) : Bitmap? =
        withContext(Dispatchers.IO) {
            mutex.withLock {
                if (closed) return@withLock null
                renderer.openPage(index).use { page ->
                    val height = (targedWidthPx.toFloat() * page.height / page.width)
                        .roundToInt().coerceAtLeast(1)
                    val bitmap = createBitmap(targedWidthPx , height)
                    bitmap.eraseColor(android.graphics.Color.WHITE)
                    page.render(bitmap , null , null , PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap
                }
            }
        }

    fun close() {
        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                if (! closed) {
                    closed = true
                    renderer.close()
                    descriptor.close()
                }
            }
        }
    }
}