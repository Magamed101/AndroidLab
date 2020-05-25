package com.example.android

import android.app.Application
import com.example.android.di.*

class MyApp : Application() {

    companion object {
        @Suppress("LateinitUsage")
        lateinit var appComponent: AppComponent
        var fscComponent: FSCComponent? = null
        var sscComponent: SSCComponent? = null
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().weatherModule(WeatherModule()).build()
    }

    fun plusFscComponent(mainActivity: MainActivity): FSCComponent =
        fscComponent ?: appComponent.plusFscComponent(MainActivityModule(mainActivity)).also {
            fscComponent = it
        }

    fun plusSscComponent(cityActivity: CityActivity): SSCComponent =
        sscComponent ?: appComponent.plusSscComponent(
            CityActivityModule(
                cityActivity
            )
        ).also {
            sscComponent = it
        }

    fun clearFSCComponent() {
        fscComponent = null
    }

    fun clearSSCComponent() {
        sscComponent = null
    }
}
