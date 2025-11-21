package com.shresht7.pockettools.ui.screens.plumbBob

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shresht7.pockettools.data.rememberUprightTilt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlumbBobScreen(navController: NavController) {
    val tilt = rememberUprightTilt()
    val animatedTilt by animateFloatAsState(
        targetValue = tilt,
        label = "PlumbBobAngleAnimation",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 50f,
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Plumb Bob") },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PlumbBobUI(angle = animatedTilt)
        }
    }
}

@Composable
fun PlumbBobUI(
    angle: Float,
    bobColor: Color = MaterialTheme.colorScheme.primary,
    bobRadius: Float = 40f,
    stringColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    stringThickness: Float = 4f,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationZ = angle
                transformOrigin = TransformOrigin(0.5f, 0.1f)   // Top Center Position
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top Center Point
            val topCenter = Offset(size.width / 2f, size.height * 0.1f)

            // Bob Position
            val bobY = size.height * 0.5f
            val bobCenter = Offset(size.width / 2f, bobY)

            // Draw String
            drawLine(
                color = stringColor,
                start = topCenter,
                end = bobCenter,
                strokeWidth = stringThickness,
            )

            // Draw Bob
            drawCircle(
                color = bobColor,
                radius = bobRadius,
                center = bobCenter
            )
        }

        Text(
            text = "%02.1f°".format(angle),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        )
    }
}