package com.jetreader.data.remote.search

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchBooksApi {
    @GET("volumes")
    suspend fun getSearchBooks(@Query("q") query: String): SearchBooksApiModel

    @GET("volumes/{bookId}")
    suspend fun getSearchBookInfo(@Path("bookId") bookId: String): SearchBooksApiModel.Item

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"
        private val contentType = "application/json".toMediaType()

        private val json = Json {
            ignoreUnknownKeys = true
        }

        fun create(): SearchBooksApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(SearchBooksApi::class.java)
    }
}
