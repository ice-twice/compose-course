package com.jetweatherforecast.data.local.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey
    @ColumnInfo
    val measurementSystem: MeasurementSystem, // TODO better to use shared prefs to save measurement system
)
