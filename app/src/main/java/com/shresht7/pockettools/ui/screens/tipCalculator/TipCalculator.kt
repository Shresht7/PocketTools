package com.shresht7.pockettools.ui.screens.tipCalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
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

    val tipAmount = { calculateTipAmount(amount, tipFraction) }
    val totalAmount = { calculateTotalAmount(amount, tipFraction) }

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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(text = tipAmount(), style = MaterialTheme.typography.displaySmall, modifier = Modifier.alpha(0.5f))
            Text(text = totalAmount(), style = MaterialTheme.typography.displayLarge)
            Spacer(modifier = Modifier.height(64.dp))

            // Amount Text Input
            TextField(
                value = amount.toString(),
                onValueChange = { value: String -> amount = value.toFloatOrNull() ?: 0.0f },
                label = { Text("Amount") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tip Percentage Input
            TextField(
                value = (tipFraction * 100).toString(),
                onValueChange = { value: String -> tipFraction = value.toFloatOrNull()?.coerceIn(0.0f, 1.0f) ?: 0.0f },
                label = { Text("Tip Percentage") },
            )
        }
    }
}

private fun calculateTipAmount(amount: Float, tipFraction: Float): String {
    val tip = amount * tipFraction
    return NumberFormat.getCurrencyInstance().format(tip)
}

private fun calculateTotalAmount(amount: Float, tipFraction: Float): String {
    val tip = amount * tipFraction
    val total = amount + tip
    return NumberFormat.getCurrencyInstance().format(total)
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalculator(navController = NavController(LocalContext.current))
}