package com.example.android.di

import com.example.android.MainActivity
import com.example.android.di.scopes.FSCScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule(val mainActivity: MainActivity) {
    @Provides
    @FSCScope
    fun fusedLocationClient(): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(mainActivity)
}
