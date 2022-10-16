package com.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jettipapp.components.InputField
import com.jettipapp.ui.theme.JetTipAppTheme
import com.jettipapp.util.calculateTipPercentage
import com.jettipapp.util.calculateTotalPerPerson
import com.jettipapp.util.calculateTotalTip
import com.jettipapp.widgets.RoundIconButton
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun MainContent() {
    val splitByState = remember { mutableStateOf(1) }
    val tipAmountState = remember { mutableStateOf(0.0) }
    val totalPerPersonState = remember { mutableStateOf(0.0) }
    BillForm(
        splitByState = splitByState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..10,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val sliderPositionState = remember { mutableStateOf(0f) }

    TopHeader(totalPerPerson = totalPerPersonState.value)
    Surface(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                    totalPerPersonState.value = calculateTotalPerPerson(
                        totalBillState.value.toDouble(),
                        splitByState.value,
                        calculateTipPercentage(sliderPositionState.value)
                    )
                }
            )
            if (validState) {
                Row(
                    modifier = modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Split")
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                splitByState.value = max(
                                    range.first,
                                    splitByState.value - 1
                                )
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    splitByState.value,
                                    calculateTipPercentage(sliderPositionState.value)
                                )
                            })
                        Text(
                            text = splitByState.value.toString(),
                            modifier = modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 9.dp)
                        )
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                splitByState.value = min(
                                    range.last,
                                    splitByState.value + 1
                                )
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    splitByState.value,
                                    calculateTipPercentage(sliderPositionState.value)
                                )
                            })
                    }
                }
                Row(modifier = modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                    Text(text = "Tip", Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(
                        text = "$" + "%.1f".format(tipAmountState.value),
                        Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = calculateTipPercentage(sliderPositionState.value).toString() + "%")
                    Spacer(modifier = Modifier.height(14.dp))
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = { newVal ->
                            sliderPositionState.value = newVal
                            tipAmountState.value =
                                calculateTotalTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = calculateTipPercentage(sliderPositionState.value)
                                )
                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitByState.value,
                                calculateTipPercentage(sliderPositionState.value)
                            )
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        steps = 5,
                    )
                }
            }
        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        MainContent()
    }
}