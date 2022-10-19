package com.movieapp.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.movieapp.model.Movie
import com.movieapp.model.getMovies
import com.movieapp.navigation.MovieScreens
import com.movieapp.widgets.MovieRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            title = {
                Text(
                    text = "Movies",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        )
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            MainContent(navController = navController)
        }
    }
}

@Composable
fun MainContent(
    navController: NavController,
    movieList: List<Movie> = getMovies()
) {
    Column(modifier = Modifier.padding(12.dp)) {
        LazyColumn {
            items(items = movieList) {
                MovieRow(it) { movie ->
                    navController.navigate(route = MovieScreens.DetailsScreen.name + "/$movie")
                }
            }
        }
    }
}