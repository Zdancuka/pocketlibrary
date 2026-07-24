package com.example.pocketlibrary.ui.screen

data class Book(
    val title: Int,
    val author: Int,
    val language: Int,
    val pageNumber: Int,
    val bookDescription: Int,
    val bookNotes: Int,
    val imageRes: Int,
    val tags: List<Int>,
)