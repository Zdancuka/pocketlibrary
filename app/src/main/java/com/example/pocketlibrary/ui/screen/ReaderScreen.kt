package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.pocketlibrary.data.local.entity.BookEntity
import com.example.pocketlibrary.ui.viewmodel.BookViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun ReaderScreen(
    book: BookEntity,
    bookViewModel: BookViewModel,
    onBack: () -> Unit
) {
    var paragraphs by remember{ mutableStateOf<List<String>?>(null) }
    var loadFailed by remember { mutableStateOf(false) }

    LaunchedEffect (book.bookId, book.contentFileName) {
        val text = bookViewModel.readBookText(book)
        if (text == null) {
            loadFailed = true
        } else {
            paragraphs = text.split("\n\n").map { it.trim() }.filter { it.isNotEmpty() }
        }
    }

    Column (modifier = Modifier.fillMaxSize()) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton (onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = Dimens.SpaceSmall)
            )
        }

        when {
            loadFailed -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.could_not_load_text))
                }
            }
            paragraphs == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn (
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Dimens.SpaceLarge)
                ) {
                    items(paragraphs!!) { paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = Dimens.SpaceMedium)
                        )
                    }
                }
            }
        }
    }
}