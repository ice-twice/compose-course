package com.jetweatherforecast.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jetweatherforecast.data.local.setting.MeasurementSystem
import com.jetweatherforecast.data.remote.api.WeatherApiModel
import com.jetweatherforecast.data.remote.api.WeatherItemApiModel
import com.jetweatherforecast.ui.navigation.WeatherScreens
import com.jetweatherforecast.ui.screens.common.WeatherAppBar
import com.jetweatherforecast.ui.screens.settings.SettingsViewModel

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    city: String?,
    settingsViewModel: SettingsViewModel
) {
    val measurementSystem = settingsViewModel.settings.collectAsStateWithLifecycle().value
    city?.let {
        LaunchedEffect(measurementSystem) {
            mainViewModel.loadWeatherData(it, measurementSystem)
        }
    }

    val viewState = mainViewModel.viewState.collectAsStateWithLifecycle()
    val viewStateValue = viewState.value
    if (viewStateValue.loading) {
        CircularProgressIndicator()
    } else if (viewStateValue.data != null) {
        Text(text = "Main Screen ${viewStateValue.data}")
        MainScaffold(navController, viewStateValue.data, measurementSystem)
    } else {
        Text(text = "Error ${viewStateValue.e}")
    }
}

@Composable
fun MainScaffold(
    navController: NavController,
    weather: WeatherApiModel,
    measurementSystem: MeasurementSystem
) {
    Scaffold(topBar = {
        WeatherAppBar(
            title = "${weather.city.name}, ${weather.city.country}",
            navController = navController,
            onAddActionClicked = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            },
        )
    }) {
        MainContent(weather, it, measurementSystem)
    }
}

@Composable
fun MainContent(
    weather: WeatherApiModel,
    paddingValues: PaddingValues,
    measurementSystem: MeasurementSystem
) {
    val weatherItem = weather.weatherItems[0]
    val imageUrl =
        "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = DateFormatter.formatDate(weatherItem.dt),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(6.dp)
        )
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(
                    text = DecimalFormatter.formatDecimal(weatherItem.temp.day) + "º",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = weatherItem.weather[0].description, fontStyle = FontStyle.Italic)
            }
        }
        HumidityWindPressureRow(weatherItem, measurementSystem)
        HorizontalDivider()
        SunsetSunriseRow(weatherItem)

        Text(
            "This Week",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(size = 14.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(2.dp), contentPadding = PaddingValues(1.dp)) {
                items(items = weather.weatherItems) { item: WeatherItemApiModel ->
                    WeatherDetailRow(weatherItem = item)
                }
            }
        }
    }
}
