package com.example.cityapiclient.di

import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.domain.usecases.GetUserFromGoogleJWT
import com.example.cityapiclient.domain.usecases.SignUserInOrOut
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Provides
    fun provideGetUserFromGoogleJWT(
        userRepository: UserRepository,
        cityApiService: CityApiService
    ): GetUserFromGoogleJWT {
        return GetUserFromGoogleJWT(userRepository, cityApiService)
    }

    @Provides
    fun provideSignUserInOrOut(
        userRepository: UserRepository
    ): SignUserInOrOut {
        return SignUserInOrOut(userRepository)
    }

}



