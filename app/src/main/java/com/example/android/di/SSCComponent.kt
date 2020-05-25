package com.example.android.di

import com.example.android.CityActivity
import com.example.android.di.scopes.SSCScope
import com.squareup.picasso.Picasso
import dagger.Subcomponent

@Subcomponent(modules = [CityActivityModule::class])
@SSCScope
interface SSCComponent {
    fun inject(cityActivity: CityActivity)
    fun getPicasso(): Picasso
}
