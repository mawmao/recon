package com.maacro.recon.feature.form.data

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine

typealias Coordinates = Pair<Double, Double> // longitude, latitude

interface CoordinatesService {
    suspend fun getCoordinates(): Coordinates
}
interface CoordinatesRepository {
    suspend fun getCoordinates(): Coordinates
    suspend fun getCoordinatesGeom(): String
}

class CoordinatesServiceImpl @Inject() constructor(
    @ApplicationContext private val context: Context
) : CoordinatesService {
    override suspend fun getCoordinates(): Coordinates = suspendCancellableCoroutine { cont ->
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            cont.resume(Coordinates(0.0, 0.0)) { _, _, _ -> }
            return@suspendCancellableCoroutine
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                cont.resume(Coordinates(location.longitude, location.latitude)) { _, _, _ ->}
                locationManager.removeUpdates(this)
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            1f,
            listener
        )

        cont.invokeOnCancellation {
            locationManager.removeUpdates(listener)
        }
    }
}

class CoordinatesRepositoryImpl @Inject constructor(
    private val coordinatesService: CoordinatesService
) : CoordinatesRepository {
    override suspend fun getCoordinates(): Coordinates = coordinatesService.getCoordinates()
    override suspend fun getCoordinatesGeom(): String {
        val coordinates = coordinatesService.getCoordinates()
        return "POINT(${coordinates.first} ${coordinates.second})"
    }
}