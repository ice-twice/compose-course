package com.movieapp.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.movieapp.model.Movie
import com.movieapp.model.getMovies

@Preview
@Composable
fun MovieRow(movie: Movie = getMovies()[0], onItemClick: (String) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onItemClick(movie.id) },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .size(100.dp),
                shape = RectangleShape,
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.poster)
                        .crossfade(true)
                        .transformations(CircleCropTransformation())
                        .build(),
                    contentDescription = "Movie Poster"
                )
            }
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Director: ${movie.director}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = "Released: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
                AnimatedVisibility(visible = expanded) {
                    Column {
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 13.sp)) {
                                append("Plot: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.DarkGray,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Light
                                )
                            ) {
                                append(movie.plot)
                            }
                        }, modifier = Modifier.padding(6.dp))
                        Divider(modifier = Modifier.padding(3.dp))
                        Text(
                            text = "Genre: ${movie.genre}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Actors: ${movie.actors}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Rating: ${movie.rating}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Down Arrow",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { expanded = !expanded },
                    tint = Color.DarkGray
                )
            }
        }
    }
}