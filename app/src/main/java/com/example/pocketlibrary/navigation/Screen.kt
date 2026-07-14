package com.example.pocketlibrary.navigation

sealed class Screen(val route: String) {

    data object Tags : Screen("tag_search")
    data object Library : Screen("library")
    data object AddBook : Screen("add_book")
}