package com.jetweatherforecast.data.local.favourite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Query(value = "SELECT * FROM favourites")
    fun subscribeFavourites(): Flow<List<FavouriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favouriteEntity: FavouriteEntity)

    @Delete
    suspend fun deleteFavourite(favouriteEntity: FavouriteEntity)

}
