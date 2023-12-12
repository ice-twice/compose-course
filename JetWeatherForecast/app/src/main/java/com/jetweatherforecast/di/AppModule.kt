package com.jetweatherforecast.di

import android.content.Context
import com.jetweatherforecast.data.local.WeatherDatabase
import com.jetweatherforecast.data.local.favourite.FavouriteDao
import com.jetweatherforecast.data.local.setting.SettingDao
import com.jetweatherforecast.data.remote.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase =
        WeatherDatabase.create(context)

    @Provides
    @Singleton
    fun provideFavouriteDao(weatherDatabase: WeatherDatabase): FavouriteDao =
        weatherDatabase.favouriteDao()

    @Provides
    @Singleton
    fun provideSettingDao(weatherDatabase: WeatherDatabase): SettingDao =
        weatherDatabase.settingDao()

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi = WeatherApi.create()
}
