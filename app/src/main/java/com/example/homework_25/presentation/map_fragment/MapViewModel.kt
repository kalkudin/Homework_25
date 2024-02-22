package com.example.homework_25.presentation.map_fragment

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_25.domain.usecase.GetPlaceCoordinatesUseCase
import com.example.homework_25.domain.usecase.GetUserLocationUseCase
import com.example.homework_25.presentation.event.MapFragmentEvent
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val getPlaceCoordinatesUseCase: GetPlaceCoordinatesUseCase
) :
    ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    private val _searchResultLocation = MutableStateFlow<LatLng?>(null)
    val searchResultLocation: StateFlow<LatLng?> = _searchResultLocation.asStateFlow()

    fun onEvent(event: MapFragmentEvent) {
        when (event) {
            is MapFragmentEvent.GetUserCoordinates -> getUserCoordinates()
            is MapFragmentEvent.GetPlaceCoordinatesByName -> getPlaceCoordinates(name = event.locationName)
        }
    }

    private fun getUserCoordinates() {
        viewModelScope.launch {
            val locationResult = getUserLocationUseCase()
            _location.value = locationResult
            Log.d("MapViewModel", "User Location is : $location")
        }
    }

    private fun getPlaceCoordinates(name: String) {
        viewModelScope.launch {
            getPlaceCoordinatesUseCase(name = name)
                .map { result ->
                    result.getOrElse { null }
                }
                .catch { e ->
                    Log.e("MapViewModel", "Error getting place coordinates: $e", e)
                }
                .collect { latLng ->
                    _searchResultLocation.value = latLng
                    latLng?.let {
                        Log.d("MapViewModel", "Search Result Location is : $latLng")
                    } ?: Log.d("MapViewModel", "Failed to find location for $name")
                }
        }
    }
}