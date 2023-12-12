package com.jetweatherforecast.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.jetweatherforecast.R
import com.jetweatherforecast.data.local.setting.MeasurementSystem
import com.jetweatherforecast.data.remote.api.WeatherItemApiModel
import java.time.format.DateTimeFormatter

@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl), contentDescription = "icon image",
        modifier = Modifier.size(80.dp)
    )
}

@Composable
fun HumidityWindPressureRow(
    weatherItem: WeatherItemApiModel,
    measurementSystem: MeasurementSystem
) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = "humidity icon",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "${weatherItem.humidity}%",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row {
            Icon(
                painter = painterResource(id = R.drawable.pressure),
                contentDescription = "pressure icon",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "${weatherItem.pressure} psi",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row {
            Icon(
                painter = painterResource(id = R.drawable.wind),
                contentDescription = "wind icon",
                modifier = Modifier.size(20.dp)
            )

            val unit = when (measurementSystem) {
                MeasurementSystem.IMPERIAL -> "mph"
                MeasurementSystem.METRIC -> "m/s"
            }
            Text(
                text = "${DecimalFormatter.formatDecimal(weatherItem.speed)} $unit",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun SunsetSunriseRow(weatherItem: WeatherItemApiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "sunrise",
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = DateFormatter.formatDateTime(weatherItem.sunrise),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "sunset",
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = DateFormatter.formatDateTime(weatherItem.sunset),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun WeatherDetailRow(weatherItem: WeatherItemApiModel) {
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Surface(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape.copy(topEnd = CornerSize(6.dp)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = DateFormatter.formatDate(
                    timestamp = weatherItem.dt,
                    formatter = DateTimeFormatter.ofPattern("E")
                ),
                modifier = Modifier.padding(start = 5.dp)
            )
            WeatherStateImage(imageUrl = imageUrl)
            Surface(
                modifier = Modifier.padding(0.dp),
                shape = CircleShape,
                color = Color(0xFFFFC400)
            ) {
                Text(
                    weatherItem.weather[0].description,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(DecimalFormatter.formatDecimal(weatherItem.temp.max) + "º")
                }
                withStyle(style = SpanStyle(color = Color.LightGray)) {
                    append(DecimalFormatter.formatDecimal(weatherItem.temp.min) + "º")
                }
            })
        }
    }
}
