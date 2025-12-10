package com.shresht7.pockettools.ui.screens.geolocation

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FilterHdr
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GeolocationScreen(
    navController: NavController,
    viewModel: GeolocationViewModel = createGeolocationViewModel()
) {
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Geolocation") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (locationPermissions.allPermissionsGranted) {
                val state by viewModel.state.collectAsState()

                DisposableEffect(Unit) {
                    viewModel.startLocationUpdates()
                    onDispose {
                        viewModel.stopLocationUpdates()
                    }
                }

                if (state.isFetching) {
                    CircularProgressIndicator()
                } else {
                    GeolocationCard(
                        title = "Latitude",
                        value = String.format("%.2f", state.latitude),
                        unit = "°",
                        icon = Icons.Outlined.Place
                    )
                    GeolocationCard(
                        title = "Longitude",
                        value = String.format("%.2f", state.longitude),
                        unit = "°",
                        icon = Icons.Outlined.Explore
                    )
                    GeolocationCard(
                        title = "Altitude",
                        value = String.format("%.2f", state.altitude),
                        unit = "m",
                        icon = Icons.Outlined.FilterHdr
                    )

                    state.address?.let {
                        GeolocationCard(
                            title = "Address",
                            value = it.getAddressLine(0),
                            unit = "",
                            icon = Icons.Outlined.Map
                        )
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Location permissions are required to use this feature.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { locationPermissions.launchMultiplePermissionRequest() }) {
                        Text("Grant Permissions")
                    }
                }
            }
        }
    }
}
