package com.shresht7.pockettools.ui.screens.spiritLevel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shresht7.pockettools.sensor.rememberOrientation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpiritLevelScreen(navController: NavController) {
    val orientation = rememberOrientation()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Spirit Level") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(8.dp, 8.dp, 8.dp, 16.dp)
        ) {

            HorizontalSpiritLevel(
                orientation.roll,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(28.dp)
                    .align(Alignment.TopCenter)
            )

            VerticalSpiritLevel(
                orientation.pitch,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .width(28.dp)
                    .align(Alignment.CenterEnd)
            )

            SpiritLevel(orientation)

            Readouts(
                orientation,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}