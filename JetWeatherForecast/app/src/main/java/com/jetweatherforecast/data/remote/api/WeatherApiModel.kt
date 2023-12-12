package com.jetweatherforecast.data.remote.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherApiModel(
    @SerialName("city")
    val city: City,
    @SerialName("cnt")
    val cnt: Int,
    @SerialName("cod")
    val cod: String,
    @SerialName("list")
    val weatherItems: List<WeatherItemApiModel>,
    @SerialName("message")
    val message: Float
) {
    @Serializable
    data class City(
        @SerialName("coord")
        val coord: Coord,
        @SerialName("country")
        val country: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("population")
        val population: Int,
        @SerialName("timezone")
        val timezone: Int
    ) {
        @Serializable
        data class Coord(
            @SerialName("lat")
            val lat: Float,
            @SerialName("lon")
            val lon: Float
        )
    }
}
