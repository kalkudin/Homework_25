package com.example.homework_25.domain.repository

import android.location.Location

interface UserLocationRepository {
    suspend fun getUserLocation(): Location?
}