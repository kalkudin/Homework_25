package com.example.homework_25.domain.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface PlaceLocationRepository {
    fun getCoordinates(locationName: String): Flow<Result<LatLng>>
}