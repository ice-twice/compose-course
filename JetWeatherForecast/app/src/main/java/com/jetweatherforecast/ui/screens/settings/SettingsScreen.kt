package com.jetweatherforecast.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jetweatherforecast.data.local.setting.MeasurementSystem
import com.jetweatherforecast.data.local.setting.SettingEntity
import com.jetweatherforecast.ui.screens.common.WeatherAppBar

@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val metricSystem = settingsViewModel.settings.collectAsStateWithLifecycle()
    var unitToggleState by remember { mutableStateOf(false) }
    var choiceState by remember { mutableStateOf(metricSystem.value) }

    Scaffold(topBar = {
        WeatherAppBar(
            title = "Settings",
            icon = Icons.Default.ArrowBack,
            isMainScreen = false,
            navController = navController
        ) { navController.popBackStack() }
    }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Units of Measurement",
                    modifier = Modifier.padding(bottom = 15.dp)
                )
                IconToggleButton(
                    checked = !unitToggleState,
                    onCheckedChange = { isChecked ->
                        unitToggleState = !isChecked
                        choiceState = if (unitToggleState) {
                            MeasurementSystem.IMPERIAL
                        } else {
                            MeasurementSystem.METRIC
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(shape = RectangleShape)
                        .padding(5.dp)
                        .background(
                            Color.LightGray.copy(alpha = 0.4f)
                        )
                ) {
                    val text = if (unitToggleState) "Fahrenheit" else "Celsius"
                    Text(text)
                }
                Button(
                    onClick = {
                        settingsViewModel.changeMeasurementUnit(
                            SettingEntity(
                                measurementSystem = choiceState
                            )
                        )
                    },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFBE42))
                ) {
                    Text(
                        text = "Save",
                        modifier = Modifier.padding(4.dp),
                        color = Color.White,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}
