package com.jetreader.data.remote.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchBooksApiModel(
    @SerialName("items")
    val items: List<Item>,
) {
    @Serializable
    data class Item(
        val id: String,
        val volumeInfo: VolumeInfo,
    ) {
        @Serializable
        data class VolumeInfo(
            val authors: List<String>? = null,
            val description: String? = null,
            val title: String,
            val imageLinks: ImageLinks? = null,
            val publishedDate: String,
            val categories: List<String>? = null,
            val pageCount: Int,
        ) {
            @Serializable
            data class ImageLinks(
                val thumbnail: String,
            )
        }
    }
}
