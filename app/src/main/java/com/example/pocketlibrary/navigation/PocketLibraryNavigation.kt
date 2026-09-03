package com.example.pocketlibrary.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.pocketlibrary.PocketLibraryApplication
import com.example.pocketlibrary.ui.screen.navbarScreens.AddBookScreenVisual
import com.example.pocketlibrary.ui.screen.AuthScreen
import com.example.pocketlibrary.ui.screen.BookDetailsScreen
import com.example.pocketlibrary.ui.screen.EditBookScreenVisual
import com.example.pocketlibrary.ui.screen.navbarScreens.LibraryScreen
import com.example.pocketlibrary.ui.screen.navbarScreens.ProfileScreen
import com.example.pocketlibrary.ui.screen.navbarScreens.SearchScreen
import com.example.pocketlibrary.ui.viewmodel.AuthViewModel
import com.example.pocketlibrary.ui.viewmodel.BookViewModel

@Composable
fun PocketLibraryNavigation() {

    val navController = rememberNavController()

    val app = LocalContext.current.applicationContext as PocketLibraryApplication

    val bookViewModel: BookViewModel= viewModel(
        factory = BookViewModel.Factory(app.bookRepository)
    )

    val authViewModel: AuthViewModel = viewModel()

    val startDestination = if (authViewModel.currentUser != null){
        Screen.Library.route
    } else {
        Screen.Auth.route
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Auth.route){
                BottomBar(navController = navController)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {

            composable (Screen.Auth.route){
                AuthScreen(
                    authViewModel = authViewModel,
                    onAuthSuccess = {
                        bookViewModel.syncFromRemote()
                        navController.navigate(Screen.Library.route){
                            popUpTo (Screen.Auth.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    bookViewModel = bookViewModel,
                    onBookClick = {
                        bookWithTags->
                        navController.navigate(
                            Screen.Details.createRoute(bookWithTags.book.bookId)
                        )
                    }
                )
            }

            composable(Screen.Library.route) {
                LibraryScreen(
                    bookViewModel = bookViewModel,
                    onBookClick = { bookWithTags ->
                        navController.navigate(
                            Screen.Details.createRoute(bookWithTags.book.bookId
                            )
                        )
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

            composable (Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onSignedOut = {
                        navController.navigate(Screen.Auth.route){
                            popUpTo (0){inclusive = true}
                        }
                    }
                )
            }

            composable (
                route = Screen.Details.route,
                arguments = listOf(navArgument("bookId"){
                    type = NavType.StringType })
                ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                val bookWithTags by bookViewModel.bookFlow(bookId).collectAsState(initial = null)

                bookWithTags?.let { details ->
                    BookDetailsScreen(
                        bookWithTags = details,
                        bookViewModel = bookViewModel,
                        navController = navController
                    )
                }
            }

            composable (
                route = Screen.Edit.route,
                arguments = listOf(navArgument("bookId"){
                    type = NavType.StringType })
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                val bookWithTags by bookViewModel.bookFlow(bookId).collectAsState(initial = null)

                bookWithTags?.let { details ->
                    EditBookScreenVisual(
                        bookWithTags = details,
                        bookViewModel = bookViewModel,
                        onBookUpdate = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
