package com.shresht7.pockettools.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ToolCard(
    onClick: () -> Unit,
    borderStroke: BorderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit,
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(borderStroke, shape),
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ToolCardPreview() {
    ToolCard(onClick = {}) {
        Text(
            text = "Counter",
            modifier = Modifier
                .padding(16.dp),
        )
    }
}