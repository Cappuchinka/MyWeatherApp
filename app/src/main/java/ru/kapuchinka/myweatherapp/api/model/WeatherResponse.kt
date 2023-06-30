package ru.kapuchinka.myweatherapp.api.model

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<Weather>,
    val city: City
)
