package com.example.pocketlibrary.navigation

sealed class Screen(val route: String) {

    data object Search : Screen("search")
    data object Library : Screen("library")
    data object AddBook : Screen("add_book")

    object Details : Screen("book_details/{bookId}") {
        fun createRoute(bookId: Long) = "book_details/$bookId"
    }

    object Edit : Screen("edit_book/{bookId}") {
        fun createRoute(bookId: Long) = "edit_book/$bookId"
    }
}