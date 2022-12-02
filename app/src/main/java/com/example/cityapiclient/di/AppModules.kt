package com.example.cityapiclient.di

import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.domain.interfaces.ICityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Singleton
    @Provides
    fun provideCityRepository(cityApiService: CityApiService): ICityRepository {
        return CityRepository(cityApiService)
    }

}



