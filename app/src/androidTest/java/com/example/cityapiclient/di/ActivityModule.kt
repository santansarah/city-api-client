package com.example.cityapiclient.di

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.di.AppModules
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.domain.interfaces.ICityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.spyk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [ActivityComponent::class],
    replaces = [ActivityModules::class]
)
class TestActivityModule {

    @Provides
    fun provideSignInObserver(
        @ActivityContext activity: Context,
        userRepository: UserRepository
    ): SignInObserver {
        return SignInObserver(activity, userRepository)
    }


}