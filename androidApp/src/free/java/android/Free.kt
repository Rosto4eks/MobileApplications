package com.example.abobapp.android

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


@Composable
fun FieldsPart(units: List<String>, state: ConverterState) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column {
        TextField(
            value = state.inputValue,
            onValueChange = { state.inputValue = it },
            modifier = if (isLandscape) Modifier.padding(10.dp) else Modifier.padding(10.dp).fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            singleLine = true,
            label = { Text(state.selectedUnit1) },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.result,
            onValueChange = { state.result = it },
            modifier = if (isLandscape) Modifier.padding(10.dp) else Modifier.padding(10.dp).fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            singleLine = true,
            label = { Text(state.selectedUnit2) },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = if (isLandscape) Modifier.padding(10.dp) else Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownMenu(
                options = units,
                selectedOption = state.selectedUnit1,
                onOptionSelected = { state.selectedUnit1 = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "--->", modifier = Modifier.padding(12.dp))
            Spacer(modifier = Modifier.width(8.dp))
            DropdownMenu(
                options = units,
                selectedOption = state.selectedUnit2,
                onOptionSelected = { state.selectedUnit2 = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

}
