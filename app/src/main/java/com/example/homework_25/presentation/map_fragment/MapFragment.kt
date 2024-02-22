package com.example.homework_25.presentation.map_fragment

import android.location.Location
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.homework_25.R
import com.example.homework_25.databinding.FragmentMapLayoutBinding
import com.example.homework_25.presentation.base.BaseFragment
import com.example.homework_25.presentation.bottom_sheet_fragment.BottomSheetFragment
import com.example.homework_25.presentation.event.MapFragmentEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapLayoutBinding>(FragmentMapLayoutBinding::inflate),
    OnMapReadyCallback, BottomSheetFragment.LocationSearchListener {

    private val mapViewModel: MapViewModel by viewModels()

    private var googleMap: GoogleMap? = null


    override fun bind() {
        bindUserLocation()
        bindMapFragment()
    }

    override fun bindViewActionListeners() {
        bindBottomSheetBtn()
    }

    override fun bindObservers() {
        bindMapLocation()
        bindPlaceLocation()
    }

    private fun bindMapFragment() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun bindUserLocation() {
        mapViewModel.onEvent(MapFragmentEvent.GetUserCoordinates)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }

    private fun bindMapLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.location.collect { location ->
                    location?.let {
                        showUserLocationOnMap(it)
                    }
                }
            }
        }
    }

    private fun bindPlaceLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.searchResultLocation.collect { latLng ->
                    showPlaceLocation(latLng)
                }
            }
        }
    }

    private fun showUserLocationOnMap(location: Location) {
        val userLatLng = LatLng(location.latitude, location.longitude)
        googleMap?.apply {
            clear()
            addMarker(MarkerOptions().position(userLatLng).title("Your Location"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
        }
    }

    private fun showPlaceLocation(latLng: LatLng?) {
        latLng?.let { location ->
            googleMap?.apply {
                clear()
                addMarker(MarkerOptions().position(location).title("Searched Location"))
                animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        }
    }

    private fun bindBottomSheetBtn() {
        binding.btnShowBottomSheet.setOnClickListener {
            BottomSheetFragment().apply {
                setLocationSearchListener(this@MapFragment)
            }.show(childFragmentManager, "BottomSheetFragment")
        }
    }

    override fun onLocationSearched(locationName: String) {
        searchLocation(locationName)
    }

    private fun searchLocation(locationName: String) {
        mapViewModel.onEvent(MapFragmentEvent.GetPlaceCoordinatesByName(locationName = locationName))
    }
}