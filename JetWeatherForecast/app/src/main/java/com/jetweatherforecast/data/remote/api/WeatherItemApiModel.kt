package com.jetweatherforecast.data.remote.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherItemApiModel(
    @SerialName("clouds")
    val clouds: Int,
    @SerialName("deg")
    val deg: Int,
    @SerialName("dt")
    val dt: Int,
    @SerialName("feels_like")
    val feelsLike: FeelsLike,
    @SerialName("gust")
    val gust: Float,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("pop")
    val pop: Float,
    @SerialName("pressure")
    val pressure: Int,
    @SerialName("rain")
    val rain: Float? = null,
    @SerialName("snow")
    val snow: Float? = null,
    @SerialName("speed")
    val speed: Float,
    @SerialName("sunrise")
    val sunrise: Int,
    @SerialName("sunset")
    val sunset: Int,
    @SerialName("temp")
    val temp: Temp,
    @SerialName("weather")
    val weather: List<Weather>
) {
    @Serializable
    data class FeelsLike(
        @SerialName("day")
        val day: Float,
        @SerialName("eve")
        val eve: Float,
        @SerialName("morn")
        val morn: Float,
        @SerialName("night")
        val night: Float
    )

    @Serializable
    data class Temp(
        @SerialName("day")
        val day: Float,
        @SerialName("eve")
        val eve: Float,
        @SerialName("max")
        val max: Float,
        @SerialName("min")
        val min: Float,
        @SerialName("morn")
        val morn: Float,
        @SerialName("night")
        val night: Float
    )

    @Serializable
    data class Weather(
        @SerialName("description")
        val description: String,
        @SerialName("icon")
        val icon: String,
        @SerialName("id")
        val id: Int,
        @SerialName("main")
        val main: String
    )
}
