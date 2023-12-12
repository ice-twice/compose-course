package com.jetweatherforecast.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jetweatherforecast.data.local.favourite.FavouriteDao
import com.jetweatherforecast.data.local.favourite.FavouriteEntity
import com.jetweatherforecast.data.local.setting.SettingDao
import com.jetweatherforecast.data.local.setting.SettingEntity

@Database(
    version = 1,
    entities = [FavouriteEntity::class, SettingEntity::class],
    exportSchema = true
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun favouriteDao(): FavouriteDao
    abstract fun settingDao(): SettingDao

    companion object {
        fun create(context: Context): WeatherDatabase =
            Room.databaseBuilder(context, WeatherDatabase::class.java, "weather_database")
                .fallbackToDestructiveMigration()
                .build()
    }
}
