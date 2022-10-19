package com.movieapp.screens.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.movieapp.model.Movie
import com.movieapp.model.getMovies
import com.movieapp.widgets.MovieRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, movieId: String?) {
    val movie = getMovies().first { it.id == movieId }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            title = {
                Row(horizontalArrangement = Arrangement.Start) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        modifier = Modifier.clickable { navController.popBackStack() },
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(
                        text = "Details",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MovieRow(movie = movie)
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Text(text = "Movie Images")
                HorizontalScrollableImageView(movie)
            }
        }
    }
}

@Composable
private fun HorizontalScrollableImageView(movie: Movie) {
    LazyRow {
        items(movie.images) { image ->
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .width(240.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                AsyncImage(model = image, contentDescription = "Movie Poster")
            }
        }
    }
}