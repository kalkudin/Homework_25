package com.example.homework_25.data

import android.location.Geocoder
import android.util.Log
import com.example.homework_25.domain.repository.PlaceLocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class PlaceLocationRepositoryImpl @Inject constructor(
    private val geocoder: Geocoder
) : PlaceLocationRepository {
    override fun getCoordinates(locationName: String): Flow<Result<LatLng>> = flow {
        try {
            Log.d("Geocoding", "Attempting to geocode city: $locationName")

            val result = suspendCancellableCoroutine<Result<LatLng>> { continuation ->
                val lowerLeftLatitude = -90.0
                val lowerLeftLongitude = -180.0
                val upperRightLatitude = 90.0
                val upperRightLongitude = 180.0

                geocoder.getFromLocationName(
                    locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude
                ) { addresses ->
                    if (addresses.isNotEmpty()) {
                        val location = addresses.first()
                        Log.d("Geocoding", "Geocoding success for $locationName: Lat ${location.latitude}, Lng ${location.longitude}")
                        continuation.resume(Result.success(LatLng(location.latitude, location.longitude)))
                    } else {
                        Log.d("Geocoding", "Geocoding failed: Location not found for $locationName")
                        continuation.resume(Result.failure(Exception("Location not found")))
                    }
                }
            }

            emit(result)
        } catch (e: Exception) {
            Log.e("Geocoding", "Geocoding error for $locationName", e)
            emit(Result.failure(e))
        }
    }
}