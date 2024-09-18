package com.jetreader.data.remote.search

import com.jetreader.domain.book.SearchBook

fun SearchBooksApiModel.Item.mapToDomain(): SearchBook = SearchBook(
    id = id,
    title = volumeInfo.title,
    authors = volumeInfo.authors?.joinToString() ?: "",
    notes = volumeInfo.description,
    imageLink = volumeInfo.imageLinks?.thumbnail?.replaceFirst(
        "http:",
        "https:"
    ),
    publishedDate = volumeInfo.publishedDate,
    categories = volumeInfo.categories?.joinToString(),
    pageCount = volumeInfo.pageCount.toString(),
    description = volumeInfo.description,
)
