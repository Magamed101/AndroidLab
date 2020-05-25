package com.example.android.di

import com.example.android.MainActivity
import com.example.android.di.scopes.FSCScope
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModule::class])
@FSCScope
interface FSCComponent {
    fun inject(mainActivity: MainActivity)
}
