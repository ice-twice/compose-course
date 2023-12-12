package com.jetweatherforecast.ui.screens.common

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jetweatherforecast.data.local.favourite.FavouriteEntity
import com.jetweatherforecast.ui.navigation.WeatherScreens
import com.jetweatherforecast.ui.screens.favourites.FavouritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WeatherAppBar(
    title: String = "Title",
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 5.dp,
    navController: NavController? = null,
    favouritesViewModel: FavouritesViewModel = hiltViewModel(),
    onAddActionClicked: () -> Unit = {},
    onNavigationButtonClicked: () -> Unit = {}
) {
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog.value) {
        ShowSettingDropDownMenu(showDialog = showDialog, navController = navController)
    }

    Surface(shadowElevation = elevation, modifier = Modifier.padding(5.dp)) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
            },
            actions = {
                if (isMainScreen) {
                    IconButton(onClick = onAddActionClicked) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
                    }
                    IconButton(onClick = {
                        showDialog.value = true
                    }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "more icon")
                    }
                }
            },
            navigationIcon = {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onNavigationButtonClicked)
                    )
                }
                if (isMainScreen) {
                    val (city, country) = title
                        .split(",") // TODO terrible
                        .map(String::trim)

                    val isAlreadyFavourite =
                        favouritesViewModel.favourites.collectAsStateWithLifecycle().value.any {
                            it.city == city
                        }
                    Icon(
                        imageVector = if (isAlreadyFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(0.9f)
                            .clickable {
                                val entity = FavouriteEntity(city = city, country = country)
                                if (isAlreadyFavourite) {
                                    favouritesViewModel.removeFavourite(entity)
                                } else {
                                    favouritesViewModel.addFavourite(entity)
                                    showToast(context = context)
                                }
                            },
                        tint = Color.Red.copy(alpha = 0.6f)
                    )
                }
            })
    }
}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>, navController: NavController?) {
    var expanded by remember { mutableStateOf(true) }
    val items = listOf("About", "Favourites", "Settings")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 20.dp)
    ) {
        DropdownMenu(
            expanded = showDialog.value,
            onDismissRequest = { showDialog.value = false },
            modifier = Modifier
                .width(140.dp)
                .background(Color.White)
        ) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = @Composable {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.Info
                                    1 -> Icons.Default.FavoriteBorder
                                    2 -> Icons.Default.Settings
                                    else -> throw IllegalStateException()
                                }, contentDescription = null,
                                tint = Color.LightGray
                            )
                            Text(text = text, fontWeight = FontWeight.W300)
                        }
                    },
                    onClick = {
                        showDialog.value = false
                        navController?.navigate(
                            when (index) {
                                0 -> WeatherScreens.AboutScreen.name
                                1 -> WeatherScreens.FavouritesScreen.name
                                2 -> WeatherScreens.SettingsScreen.name
                                else -> throw IllegalStateException()
                            }
                        )
                    })
            }
        }
    }
}

private fun showToast(context: Context) {
    Toast.makeText(context, "City added to Favourites", Toast.LENGTH_SHORT).show()
}
