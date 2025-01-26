package com.example.abobapp.android

import android.content.res.Configuration
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.*
import androidx.navigation.compose.*


@Composable
fun FieldsPart(units: List<String>, state: ConverterState) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    var context = LocalContext.current;
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.inputValue,
                onValueChange = { state.inputValue = it },
                singleLine = true,
                label = { Text(state.selectedUnit1) },
                readOnly = true,
            )

            Box(
                modifier = Modifier.matchParentSize().clickable {copyToClipboard(context, state.inputValue) },
            )
        }

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                state.result = convert(state.inputValue, state.selectedUnit1, state.selectedUnit2)
                var temp = state.selectedUnit1
                state.selectedUnit1 = state.selectedUnit2
                state.selectedUnit2 = temp
                if (state.result.isNotEmpty()) {
                    temp = state.inputValue
                    state.inputValue = state.result
                    state.result = temp
                }
            }
        ) {
            Text(text = "<>", fontSize = 18.sp)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = state.result,
                onValueChange = { state.result = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(state.selectedUnit2) },
                readOnly = true
            )
            Box(
                modifier = Modifier.matchParentSize().clickable {copyToClipboard(context, state.result) },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = if (isLandscape) Modifier.padding(10.dp) else Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DropdownMenu(
                options = units,
                selectedOption = state.selectedUnit1,
                onOptionSelected = { state.selectedUnit1 = it }
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "--->", modifier = Modifier.padding(14.dp))

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

fun copyToClipboard(context: Context, text: String, label: String = "Copied Text") {
    // Get the ClipboardManager system service
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    // Create a ClipData object with the text
    val clipData = ClipData.newPlainText(label, text)

    // Set the ClipData to the clipboard
    clipboardManager.setPrimaryClip(clipData)

    // Optionally, show a toast to confirm the text was copied
    Toast.makeText(context, "Copied to clipboard: $text", Toast.LENGTH_SHORT).show()
}

