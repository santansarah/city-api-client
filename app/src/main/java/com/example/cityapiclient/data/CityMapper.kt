package com.example.cityapiclient.data

import com.example.cityapiclient.data.remote.models.CityDto
import com.example.cityapiclient.domain.models.City
import com.example.cityapiclient.domain.models.CityResults

fun CityDto.toCityResults(): CityResults =
    CityResults(
        this.zip,
        this.city,
        this.state
    )

fun List<CityDto>.toCityResultsList(): List<CityResults> =
    this.map { it.toCityResults() }

fun CityDto.toCity(): City =
    City(
        this.zip,
        this.lat,
        this.lng,
        this.city,
        this.state,
        this.population,
        this.county
    )
