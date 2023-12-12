package com.jetweatherforecast.ui.screens.favourites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jetweatherforecast.data.local.favourite.FavouriteEntity
import com.jetweatherforecast.ui.navigation.WeatherScreens
import com.jetweatherforecast.ui.screens.common.WeatherAppBar

@Composable
fun FavouritesScreen(navController: NavController, favouritesViewModel: FavouritesViewModel) {
    Scaffold(topBar = {
        WeatherAppBar(
            title = "Favourite cities",
            icon = Icons.Default.ArrowBack,
            isMainScreen = false,
            navController = navController,
            onNavigationButtonClicked = { navController.popBackStack() }
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val favourites by favouritesViewModel.favourites.collectAsStateWithLifecycle()
                LazyColumn {
                    items(favourites) { favourite ->
                        CityRow(favourite, navController = navController, favouritesViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun CityRow(
    favourite: FavouriteEntity,
    navController: NavController,
    favouritesViewModel: FavouritesViewModel
) {
    Surface(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clickable { navController.navigate(WeatherScreens.MainScreen.name + "/${favourite.city}") },
        shape = CircleShape.copy(topEnd = CornerSize(6.dp)),
        color = Color(0xFFB2DFDB)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = favourite.city, modifier = Modifier.padding(start = 4.dp))
            Surface(
                modifier = Modifier.padding(0.dp),
                shape = CircleShape,
                color = Color(0xFFD1E3E1)
            ) {
                Text(
                    text = favourite.country,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                modifier = Modifier.clickable { favouritesViewModel.removeFavourite(favourite) },
                tint = Color.Red.copy(alpha = 0.3f)
            )
        }
    }
}
