package com.jetreader.domain.book

data class SearchBook(
    val id: String,
    val title: String,
    val authors: String,
    val notes: String?,
    val imageLink: String?,
    val publishedDate: String,
    val categories: String?,
    val pageCount: String,
    val description: String?,
)
