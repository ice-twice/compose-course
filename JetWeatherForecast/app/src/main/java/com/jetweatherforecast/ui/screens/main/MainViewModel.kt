package com.jetweatherforecast.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetweatherforecast.data.WeatherRepository
import com.jetweatherforecast.data.local.setting.MeasurementSystem
import com.jetweatherforecast.data.remote.api.WeatherApiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {
    val viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())

    fun loadWeatherData(city: String, measurementSystem: MeasurementSystem) {
        viewState.update { it.copy(loading = true, e = null) }
        viewModelScope.launch {
            repository.loadWeather(city, measurementSystem)
                .onSuccess { data -> viewState.update { it.copy(data = data) } }
                .onFailure { e -> viewState.update { it.copy(e = e) } } // TODO add onErrorShowed()
            viewState.update { it.copy(loading = false) }
        }
    }

    data class ViewState(
        val data: WeatherApiModel? = null,
        val loading: Boolean = false,
        val e: Throwable? = null,
    )
}
