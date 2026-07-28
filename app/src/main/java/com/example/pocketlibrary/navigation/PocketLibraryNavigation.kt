package com.example.pocketlibrary.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.pocketlibrary.PocketLibraryApplication
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.screen.AddBookScreenVisual
import com.example.pocketlibrary.ui.screen.BookDetailsScreen
import com.example.pocketlibrary.ui.screen.LibraryScreen
import com.example.pocketlibrary.ui.screen.TagScreen
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

@Composable
fun PocketLibraryNavigation() {

    val navController = rememberNavController()

    val app = LocalContext.current.applicationContext as PocketLibraryApplication

    val bookViewModel: BookViewModel= viewModel(
        factory = BookViewModel.Factory(app.bookRepository)
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
                val books by bookViewModel.books.collectAsState()
                val firstBook = books.firstOrNull()

                if (firstBook != null){
                    TagScreen(bookWithTags = firstBook)
                }

                //Todo not books time
            }

            composable(Screen.Library.route) {
                LibraryScreen(
                    bookViewModel = bookViewModel,
                    onBookClick = { bookWithTags ->
                        navController.navigate(Screen.Details.createRoute(bookWithTags.book.bookId))
                    }
                )
            }

            composable(Screen.AddBook.route) {
                AddBookScreenVisual(
                    bookViewModel = bookViewModel,
                    onBookSaved = {
                        navController.navigate(Screen.Library.route) {
                            popUpTo(Screen.Library.route){inclusive = true}
                        }
                    }
                )
            }

            composable (
                route = Screen.Details.route,
                arguments = listOf(navArgument("bookId"){
                    type = NavType.LongType })
                ) { backStackEntry ->
                // Magic number fallback, better move to a named const for readability.
                val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0L
                val bookWithTags by bookViewModel.bookFlow(bookId).collectAsState(initial = null)

                bookWithTags?.let { details ->
                    // Typo was `detalis`.
                    BookDetailsScreen(bookWithTags = details)
                }
            }
        }
    }
}