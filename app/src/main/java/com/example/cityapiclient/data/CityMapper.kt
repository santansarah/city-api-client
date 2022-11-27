package com.example.cityapiclient.data

import com.example.cityapiclient.data.remote.models.CityApiModel
import com.example.cityapiclient.domain.models.City
import com.example.cityapiclient.domain.models.CityResults

fun CityApiModel.toCityResults(): CityResults =
    CityResults(
        this.zip,
        this.city,
        this.state
    )

fun List<CityApiModel>.toCityResultsList(): List<CityResults> =
    this.map { it.toCityResults() }

fun CityApiModel.toCity(): City =
    City(
        this.zip,
        this.lat,
        this.lng,
        this.city,
        this.state,
        this.population,
        this.county
    )
