package com.example.pocketlibrary.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.screen.AddBookScreenVisual
import com.example.pocketlibrary.ui.screen.Book
import com.example.pocketlibrary.ui.screen.LibraryScreen
import com.example.pocketlibrary.ui.screen.TagScreen

@Composable
fun PocketLibraryNavigation() {

    val navController = rememberNavController()

    val sampleBook = Book(
        title = R.string.book_title,
        author = R.string.author_name,
        language = R.string.book_en_language,
        pageNumber = R.string.book_page_number,
        bookDescription = R.string.book_description_example,
        bookNotes = R.string.book_notes_example,
        imageRes = R.drawable.book_caver_example,
        tags = listOf(R.string.tag_romantic)
    )

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Library.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.Tags.route) {
                TagScreen(book = sampleBook)
            }

            composable(Screen.Library.route) {
                LibraryScreen()
            }

            composable(Screen.AddBook.route) {
                AddBookScreenVisual()
            }
        }
    }
}