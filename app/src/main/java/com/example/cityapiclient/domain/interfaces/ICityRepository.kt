package com.example.cityapiclient.domain.interfaces

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.domain.models.City
import com.example.cityapiclient.domain.models.CityResults
import kotlinx.coroutines.flow.Flow

interface ICityRepository {

    suspend fun getCitiesByName(prefix: String): ServiceResult<List<CityResults>>
    suspend fun getCitiesByZip(zipCode: Int): ServiceResult<City>
}