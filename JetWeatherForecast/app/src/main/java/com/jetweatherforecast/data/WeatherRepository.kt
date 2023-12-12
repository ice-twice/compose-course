package com.jetweatherforecast.data

import android.util.Log
import com.jetweatherforecast.data.local.favourite.FavouriteDao
import com.jetweatherforecast.data.local.favourite.FavouriteEntity
import com.jetweatherforecast.data.local.setting.MeasurementSystem
import com.jetweatherforecast.data.local.setting.SettingDao
import com.jetweatherforecast.data.local.setting.SettingEntity
import com.jetweatherforecast.data.remote.api.WeatherApi
import com.jetweatherforecast.data.remote.api.WeatherApiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val favouriteDao: FavouriteDao,
    private val settingDao: SettingDao,
) {

    suspend fun loadWeather(
        city: String,
        measurementSystem: MeasurementSystem
    ): Result<WeatherApiModel> =
        try { // TODO do not propagate WeatherApiModel to domain
            val units = when (measurementSystem) {
                MeasurementSystem.IMPERIAL -> "imperial"
                MeasurementSystem.METRIC -> "metric"
            }
            val weather = api.loadWeather(city = city, units = units)
            Result.success(weather)
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred while loading weather: $e")
            Result.failure(e) // TODO exception must be converted to some kind of domain exception
        }


    fun subscribeFavourites(): Flow<List<FavouriteEntity>> = favouriteDao.subscribeFavourites()

    suspend fun insertFavourite(favouriteEntity: FavouriteEntity) =
        favouriteDao.insertFavourite(favouriteEntity)

    suspend fun deleteFavourite(favouriteEntity: FavouriteEntity) =
        favouriteDao.deleteFavourite(favouriteEntity)

    fun subscribeSettings(): Flow<List<SettingEntity>> = settingDao.subscribeSettings()

    suspend fun insertSetting(settingEntity: SettingEntity) =
        settingDao.insertSetting(settingEntity)

    suspend fun deleteAllSettings() = settingDao.deleteAllSettings()

    companion object {
        private val TAG = WeatherRepository::class.simpleName
    }
}
