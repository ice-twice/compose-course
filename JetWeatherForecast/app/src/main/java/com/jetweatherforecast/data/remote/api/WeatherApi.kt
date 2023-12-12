package com.jetweatherforecast.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jetweatherforecast.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast/daily")
    suspend fun loadWeather(
        @Query("q") city: String,
        @Query("appid") appId: String = BuildConfig.API_KEY,
        @Query("units") units: String,
    ): WeatherApiModel

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"
        private val contentType = "application/json".toMediaType()

        fun create(): WeatherApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(WeatherApi::class.java)
    }
}
