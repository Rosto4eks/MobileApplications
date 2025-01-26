package com.example.abobapp.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.*
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConverterApp()
        }
    }
}

@Composable
fun ConverterApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "length",
            modifier = Modifier.padding(padding)
        ) {
            composable("length") { LengthConverterScreen() }
            composable("weight") { WeightConverterScreen() }
            composable("temperature") { TemperatureConverterScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Length", "length"),
        BottomNavItem("Weight", "weight"),
        BottomNavItem("Temperature", "temperature")
    )
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {},
                label = { Text(text = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String)

@Composable
fun LengthConverterScreen() {
    ConverterScreen(
        title = "Length",
        units = listOf("Meters", "Kilometers", "Centimeters")
    )
}

@Composable
fun WeightConverterScreen() {
    ConverterScreen(
        title = "Weight",
        units = listOf("Grams", "Kilograms", "Pounds")
    )
}

@Composable
fun TemperatureConverterScreen() {
    ConverterScreen(
        title = "Temperature",
        units = listOf("Celsius", "Fahrenheit", "Kelvin")
    )
}

class ConverterState {
    var inputValue by mutableStateOf("")
    var selectedUnit1 by mutableStateOf("")
    var selectedUnit2 by mutableStateOf("")
    var result by mutableStateOf("")
}

@Composable
fun ConverterScreen(title: String, units: List<String>) {
    var state = remember { ConverterState() }
    state.selectedUnit1 = units[0]
    state.selectedUnit2 = units[1]

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
        ) {
            NumpadPart(state)

            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    state.result = convert(state.inputValue, state.selectedUnit1, state.selectedUnit2)
                }
            ) {
                Text(text = "Convert", fontSize = 18.sp)
            }

            FieldsPart(
                units = units,
                state = state,
            )

        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FieldsPart(
                units = units,
                state = state,
            )

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    state.result = convert(state.inputValue, state.selectedUnit1, state.selectedUnit2)
                }
            ) {
                Text(text = "Convert", fontSize = 18.sp)
            }
            NumpadPart(state)

        }
    }
}

@Composable
fun NumpadPart(
    state: ConverterState
) {
    Numpad { value ->
        when (value) {
            "-/+" -> {
                if (state.inputValue.isEmpty()) {}
                else if (state.inputValue[0] == '-') {
                    state.inputValue = state.inputValue.substring(1)
                }
                else {
                    state.inputValue = "-${state.inputValue}"
                }
            }
            "." -> {
                if (state.inputValue.isNotEmpty() and ! state.inputValue.contains(".")) {
                    state.inputValue += "."
                }
            }
            "-1" -> {
                if (state.inputValue.isNotEmpty()) {
                    state.inputValue = state.inputValue.substring(0, state.inputValue.length - 1)
                }
                if (state.inputValue == "-") {
                    state.inputValue = ""
                }
            }
            else -> {
                state.inputValue += value
            }
        }

    }
}

@Composable
fun DropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
            onClick = { expanded = true }
        ) {
            Text(text = selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun Numpad(onNumberClick: (String) -> Unit) {
    val numpadKeys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "<"),
        listOf("-/+")
    )

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column (
    ) {
        numpadKeys.forEach { row ->
            Row(
                modifier = if (isLandscape) Modifier else Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEach { key ->
                    Button(
                        onClick = {
                            if (key == "<") {
                                onNumberClick.invoke("-1") // Handle deletion as needed
                            } else {
                                onNumberClick.invoke(key)
                            }
                        },
                        modifier = Modifier.padding(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)
                    ) {
                        Text(text = key, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

fun convert(inputValue: String, selectedUnit1: String, selectedUnit2: String): String {
    if (inputValue.isEmpty()) {
        return ""
    }
    when (selectedUnit1) {
        "Meters" -> {
            when (selectedUnit2) {
                "Kilometers" -> return (inputValue.toDouble() / 1000).toBigDecimal().toPlainString()
                "Centimeters" -> return (inputValue.toDouble() * 100).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Kilometers" -> {
            when (selectedUnit2) {
                "Meters" -> return (inputValue.toDouble() * 1000).toBigDecimal().toPlainString()
                "Centimeters" -> return (inputValue.toDouble() * 100000).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Centimeters" -> {
            when (selectedUnit2) {
                "Meters" -> return (inputValue.toDouble() / 100).toBigDecimal().toPlainString()
                "Kilometers" -> return (inputValue.toDouble() / 100000).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Grams" -> {
            when (selectedUnit2) {
                "Kilograms" -> return (inputValue.toDouble() / 1000).toBigDecimal().toPlainString()
                "Pounds" -> return (inputValue.toDouble() / 453.592).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Kilograms" -> {
            when (selectedUnit2) {
                "Grams" -> return (inputValue.toDouble() * 1000).toBigDecimal().toPlainString()
                "Pounds" -> return (inputValue.toDouble() * 2.20462).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Pounds" -> {
            when (selectedUnit2) {
                "Grams" -> return (inputValue.toDouble() * 453.592).toBigDecimal().toPlainString()
                "Kilograms" -> return (inputValue.toDouble() / 2.20462).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Celsius" -> {
            when (selectedUnit2) {
                "Fahrenheit" -> return ((inputValue.toDouble() * 9 / 5) + 32).toBigDecimal().toPlainString()
                "Kelvin" -> return (inputValue.toDouble() + 273.15).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Fahrenheit" -> {
            when (selectedUnit2) {
                "Celsius" -> return ((inputValue.toDouble() - 32) * 5 / 9).toBigDecimal().toPlainString()
                "Kelvin" -> return ((inputValue.toDouble() - 32) * 5 / 9 + 273.15).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        "Kelvin" -> {
            when (selectedUnit2) {
                "Celsius" -> return (inputValue.toDouble() - 273.15).toBigDecimal().toPlainString()
                "Fahrenheit" -> return ((inputValue.toDouble() - 273.15) * 9 / 5 + 32).toBigDecimal().toPlainString()
                else -> return inputValue
            }
        }
        else -> return inputValue
    }
}