package com.shresht7.pockettools.ui.screens.geolocation

import android.location.Address

data class GeolocationState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
    val address: Address? = null,
    val isFetching: Boolean = false,
)