package com.example.pocketlibrary.navigation

sealed class Screen(val route: String) {

    object Tags : Screen("tag_search")
    object Library : Screen("library")
    object AddBook : Screen("add_book")
}