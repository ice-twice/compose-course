package com.jettrivia.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET

interface QuestionApi {
    @GET("world.json")
    suspend fun loadAllQuestions(): List<QuestionItem>

    companion object {
        private const val BASE_URL =
            "https://raw.githubusercontent.com/itmmckernan/triviaJSON/master/"
        private val contentType = MediaType.get("application/json")

        @OptIn(ExperimentalSerializationApi::class)
        fun create(): QuestionApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(QuestionApi::class.java)
    }
}