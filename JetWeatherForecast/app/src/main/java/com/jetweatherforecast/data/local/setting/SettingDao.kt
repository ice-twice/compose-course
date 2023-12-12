package com.jetweatherforecast.data.local.setting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {

    @Query(value = "SELECT * FROM settings")
    fun subscribeSettings(): Flow<List<SettingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(settingEntity: SettingEntity)

    @Query(value = "DELETE from settings")
    suspend fun deleteAllSettings()

}
