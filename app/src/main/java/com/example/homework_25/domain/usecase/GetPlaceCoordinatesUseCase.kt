package com.example.homework_25.domain.usecase

import com.example.homework_25.domain.repository.PlaceLocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlaceCoordinatesUseCase @Inject constructor(private val placeLocationRepository: PlaceLocationRepository) {
    operator fun invoke(name : String) : Flow<Result<LatLng>> {
        return placeLocationRepository.getCoordinates(name)
    }
}