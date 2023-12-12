package com.jetweatherforecast.data.local.favourite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey
    @ColumnInfo
    val city: String,

    @ColumnInfo
    val country: String,
)
