package com.shresht7.pockettools.ui.screens.geolocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class GeolocationViewModel(
    private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(GeolocationState())
    val state: StateFlow<GeolocationState> = _state

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                _state.value = _state.value.copy(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    altitude = location.altitude,
                    isFetching = false
                )
                fetchAddress(location.latitude, location.longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        _state.value = _state.value.copy(isFetching = true)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(10000)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        _state.value = _state.value.copy(isFetching = false)
    }

    private fun fetchAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses?.isNotEmpty() == true) {
                    _state.value = _state.value.copy(address = addresses[0])
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}

class GeolocationViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeolocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GeolocationViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun createGeolocationViewModel(): GeolocationViewModel {
    val context = LocalContext.current
    return viewModel(factory = GeolocationViewModelFactory(context.applicationContext))
}
