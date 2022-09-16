package com.example.cityapiclient.di

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.ICityApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModules {
    @Singleton
    @Provides
    fun provideKtorClient(): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HTTP Client", message)
                    }
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
/*            HttpResponseValidator {
                validateResponse { response ->
                    val error: Error = response.body()
                    if (error.code != 0) {
                        throw CustomResponseException(response, "Code: ${error.code}, message: ${error.message}")
                    }
                }
            }*/
        }
    }

    @Singleton
    @Provides
    fun provideCityApiService(client: HttpClient): ICityApiService {
        return CityApiService(client)
    }

}