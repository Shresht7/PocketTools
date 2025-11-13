package com.shresht7.pockettools.ui.screens.tipCalculator

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculator(navController: NavController) {
    var amount by remember { mutableFloatStateOf(100.0f) }
    var tipFraction by remember { mutableFloatStateOf(0.15f) }
    var roundUp by remember { mutableStateOf(false) }

    val tipAmount = { calculateTipAmount(amount, tipFraction, roundUp) }
    val totalAmount = { calculateTotalAmount(amount, tipFraction, roundUp) }


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Tip Calculator") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(text = tipAmount(), style = MaterialTheme.typography.displaySmall, modifier = Modifier.alpha(0.5f))
            Text(text = totalAmount(), style = MaterialTheme.typography.displayLarge)
            Spacer(modifier = Modifier.height(64.dp))

            // Amount Text Input
            TextField(
                value = String.format("%.2f", amount),
                onValueChange = { value: String -> amount = value.toFloatOrNull() ?: 0.0f },
                label = { Text("Amount") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                leadingIcon = { Text("$") },
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tip Percentage Input
            TextField(
                value = String.format("%.2f", tipFraction * 100),
                onValueChange = { value: String -> tipFraction = value.toFloatOrNull()?.coerceIn(0.0f, 1.0f) ?: 0.0f },
                label = { Text("Tip Percentage") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done ),
                leadingIcon = { Text("%") }
            )

            Row(
               horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Round Up Tip?")
                Switch(
                    checked = roundUp,
                    onCheckedChange = { roundUp = it },
                )
            }
        }
    }
}

@VisibleForTesting
internal fun calculateTipAmount(amount: Float, tipFraction: Float, roundUp: Boolean): String {
    var tip = amount * tipFraction
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@VisibleForTesting
internal fun calculateTotalAmount(amount: Float, tipFraction: Float, roundUp: Boolean): String {
    var tip = amount * tipFraction
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    val total = amount + tip
    return NumberFormat.getCurrencyInstance().format(total)
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalculator(navController = NavController(LocalContext.current))
}