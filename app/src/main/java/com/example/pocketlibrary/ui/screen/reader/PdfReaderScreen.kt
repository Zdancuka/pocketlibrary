package com.example.pocketlibrary.ui.screen.reader

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.BookTextStorage
import com.example.pocketlibrary.data.local.PdfPageRenderer
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.ui.theme.Dimens
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

@Composable
fun PdfReaderScreen(
    book: BookEntity,
    onBack: ()-> Unit
){
    val context = LocalContext.current
    val fileName = book.contentFileName

    var renderer by remember (fileName){ mutableStateOf<PdfPageRenderer?>(null) }
    var failed by remember (fileName){ mutableStateOf(false) }

    LaunchedEffect (fileName) {
        if (fileName == null) {
            failed = true
            return@LaunchedEffect
        }
        val opened = withContext(Dispatchers.IO) {
            runCatching { PdfPageRenderer(BookTextStorage.getFile(context, fileName)) }.getOrNull()
        }
        when {
            opened == null -> failed = true
            !isActive -> opened.close()
            else -> renderer = opened
        }
    }

        DisposableEffect (key1 = fileName) {
            onDispose { renderer?.close() }
        }

        val density = LocalDensity.current
        val screenWithDp = LocalConfiguration.current.screenWidthDp
        val widthPx = remember (screenWithDp, density){
            with(density){ screenWithDp.dp.roundToPx()}.coerceAtMost(1600)
        }

        val listState = rememberLazyListState()
        val current = renderer

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpaceMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow. Ellipsis,
                    modifier = Modifier
                        .padding(start = Dimens.SpaceSmall)
                        .weight(1f)
                )
                if (current != null){
                    Text(
                        text = stringResource(
                            R.string.pdf_page_counter,
                            listState.firstVisibleItemIndex +1,
                            current.pageCount
                        ), style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = Dimens.SpaceSmall)
                    )
                }
            }

            when{
                failed-> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(R.string.could_not_open_pdf),
                        modifier = Modifier.padding(Dimens.SpaceLarge)
                    )
                }

                current == null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }

                else -> LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
                ) {
                    items ( current.pageCount ){ index ->
                        PdfPade(renderer = current, index = index, widthPx = widthPx)
                    }
                }
            }
        }
}


@Composable
private fun PdfPade(
    renderer: PdfPageRenderer,
    index: Int,
    widthPx: Int
){
    var bitmap by remember (renderer, index, widthPx){mutableStateOf<Bitmap?>(null)  }

    LaunchedEffect (renderer, index, widthPx) {
        bitmap = try{
            renderer.renderPage(index, widthPx)
        } catch (e: CancellationException){
            throw e
        } catch (e: Exception){
            null
        }

    }

    val renderered = bitmap
    if (renderered != null){
        Image(
            bitmap = renderered.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1.414f)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

    }
}