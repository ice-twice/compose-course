package com.jetweatherforecast.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetweatherforecast.data.WeatherRepository
import com.jetweatherforecast.data.local.favourite.FavouriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {
    val favourites: MutableStateFlow<List<FavouriteEntity>> = MutableStateFlow(emptyList())

    init {
        repository.subscribeFavourites()
            .onEach { favourites ->
                this.favourites.update { favourites }
            }
            .launchIn(viewModelScope)
    }

    fun addFavourite(favouriteEntity: FavouriteEntity) =
        viewModelScope.launch { repository.insertFavourite(favouriteEntity) }

    fun removeFavourite(favouriteEntity: FavouriteEntity) =
        viewModelScope.launch { repository.deleteFavourite(favouriteEntity) }
}
