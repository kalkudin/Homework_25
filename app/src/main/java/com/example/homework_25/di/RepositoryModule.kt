package com.example.homework_25.di

import android.app.Application
import android.location.Geocoder
import com.example.homework_25.data.PlaceLocationRepositoryImpl
import com.example.homework_25.data.UserLocationRepositoryImpl
import com.example.homework_25.domain.repository.PlaceLocationRepository
import com.example.homework_25.domain.repository.UserLocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserLocationRepository(fusedLocationProviderClient: FusedLocationProviderClient, application: Application): UserLocationRepository {
        return UserLocationRepositoryImpl(fusedLocationProviderClient, application)
    }
    @Provides
    @Singleton
    fun providePlaceLocationRepository(geocoder: Geocoder) : PlaceLocationRepository {
        return PlaceLocationRepositoryImpl(geocoder = geocoder)
    }
}