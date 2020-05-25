package com.example.android.di

import com.example.android.BuildConfig
import com.example.android.WeatherService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class WeatherModule {

    @Provides
    @Singleton
    fun weatherService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Provides
    fun retrofit(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun okHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun authInterceptor(): Interceptor =
        Interceptor { chain ->

            val newUrl = chain.request().url().newBuilder()
                .addQueryParameter("units", "metric")
                .addQueryParameter("appid", BuildConfig.API_KEY)
                .build()

            val newRequest = chain.request().newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }
}
