package com.example.android.di

import com.example.android.CityActivity
import com.example.android.di.scopes.SSCScope
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class CityActivityModule(val cityActivity: CityActivity) {
    @Provides
    @SSCScope
    fun getPicasso(): Picasso =
        Picasso.Builder(cityActivity).build()
}
