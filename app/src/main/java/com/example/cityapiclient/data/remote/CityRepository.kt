package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.toCity
import com.example.cityapiclient.data.toCityResultsList
import com.example.cityapiclient.domain.interfaces.ICityRepository
import com.example.cityapiclient.domain.models.City
import com.example.cityapiclient.domain.models.CityResults
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityApiService: CityApiService
) : ICityRepository {

    fun close() {
        cityApiService.close()
    }

    override suspend fun getCitiesByName(prefix: String): ServiceResult<List<CityResults>> {
        Log.d("debug", "apiThread: ${Thread.currentThread()}")
        return when (val cityApiResult = cityApiService.getCitiesByName(prefix)) {
            is ServiceResult.Success -> {
                ServiceResult.Success(cityApiResult.data.cities.toCityResultsList())
            }
            is ServiceResult.Error -> {
                Log.d("debug", "api error: ${cityApiResult.message}")
                cityApiResult
            }
        }
    }

    override suspend fun getCitiesByZip(zipCode: Int): ServiceResult<City> =
        when (val cityApiResult = cityApiService.getCityByZip(zipCode)) {
            is ServiceResult.Success -> {
                ServiceResult.Success(cityApiResult.data.cities[0].toCity())
            }
            is ServiceResult.Error -> {
                Log.d("debug", "api error: ${cityApiResult.message}")
                cityApiResult
            }
        }

}