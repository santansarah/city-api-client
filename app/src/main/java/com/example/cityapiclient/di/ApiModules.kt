package com.example.cityapiclient.di

import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.domain.interfaces.ICityApiService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModules {

    @Singleton
    @Binds
    abstract fun bindCityApiService(
        cityApiService: CityApiService
    ): ICityApiService
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher