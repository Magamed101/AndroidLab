package com.example.android.di

import com.example.android.WeatherService
import dagger.Component
import javax.inject.Singleton

@Component(modules = [WeatherModule::class])
@Singleton
interface AppComponent {
    fun getWeatherService(): WeatherService
    fun plusFscComponent(mainActivityModule: MainActivityModule): FSCComponent
    fun plusSscComponent(cityActivityModule: CityActivityModule): SSCComponent
}
