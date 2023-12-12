package com.jetweatherforecast.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetweatherforecast.data.WeatherRepository
import com.jetweatherforecast.data.local.setting.MeasurementSystem
import com.jetweatherforecast.data.local.setting.SettingEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {
    val settings: MutableStateFlow<MeasurementSystem> = MutableStateFlow(MeasurementSystem.METRIC)

    init {
        repository.subscribeSettings()
            .mapNotNull { it.firstOrNull()?.measurementSystem }
            .onEach(settings::emit)
            .launchIn(viewModelScope)
    }

    fun changeMeasurementUnit(settingEntity: SettingEntity) {
        viewModelScope.launch {
            repository.deleteAllSettings()
            repository.insertSetting(settingEntity)
        }
    }
}
