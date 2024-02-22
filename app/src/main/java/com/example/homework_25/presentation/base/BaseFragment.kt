package com.example.homework_25.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias inflater<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB

abstract class BaseFragment<VB : ViewBinding>(private val inflate: inflater<VB>) : Fragment() {

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

//    private val locationPermissionRequest = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val granted = permissions.entries.all { it.value }
//        if (granted) {
//            onLocationPermissionGranted()
//        } else {
//            onLocationPermissionDenied()
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        bindViewActionListeners()
        bindObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun bind() {}

    abstract fun bindViewActionListeners()

    abstract fun bindObservers()

    open fun shouldRequestLocationPermission(): Boolean {
        return false
    }

//    private fun requestLocationPermission() {
//        locationPermissionRequest.launch(
//            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//        )
//    }
//
//    open fun onLocationPermissionGranted() {
//        Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()
//    }
//
//    open fun onLocationPermissionDenied() {
//        Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
//    }
}