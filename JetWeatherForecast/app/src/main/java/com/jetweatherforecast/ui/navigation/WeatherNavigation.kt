package com.jetweatherforecast.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jetweatherforecast.ui.screens.about.AboutScreen
import com.jetweatherforecast.ui.screens.favourites.FavouritesScreen
import com.jetweatherforecast.ui.screens.favourites.FavouritesViewModel
import com.jetweatherforecast.ui.screens.main.MainScreen
import com.jetweatherforecast.ui.screens.main.MainViewModel
import com.jetweatherforecast.ui.screens.search.SearchScreen
import com.jetweatherforecast.ui.screens.settings.SettingsScreen
import com.jetweatherforecast.ui.screens.settings.SettingsViewModel
import com.jetweatherforecast.ui.screens.splash.WeatherSplashScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WeatherScreens.SplashScreen.name
    ) {
        composable(WeatherScreens.SplashScreen.name) {
            WeatherSplashScreen(navController = navController)
        }
        val route = WeatherScreens.MainScreen.name
        composable(
            "$route/{city}", arguments = listOf(
                navArgument(name = "city") {
                    type = NavType.StringType
                })
        ) { navBackStack ->
            val city = navBackStack.arguments?.getString("city")
            val mainViewModel = hiltViewModel<MainViewModel>()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            MainScreen(navController = navController, mainViewModel, city = city, settingsViewModel)
        }
        composable(WeatherScreens.SearchScreen.name) {
            SearchScreen(navController = navController)
        }
        composable(WeatherScreens.AboutScreen.name) {
            AboutScreen(navController = navController)
        }
        composable(WeatherScreens.FavouritesScreen.name) {
            val favouritesViewModel = hiltViewModel<FavouritesViewModel>()
            FavouritesScreen(
                navController = navController,
                favouritesViewModel = favouritesViewModel
            )
        }
        composable(WeatherScreens.SettingsScreen.name) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(navController = navController, settingsViewModel)
        }
    }
}
