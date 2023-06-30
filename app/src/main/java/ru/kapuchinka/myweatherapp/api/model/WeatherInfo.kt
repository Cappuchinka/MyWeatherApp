package ru.kapuchinka.myweatherapp.api.model

data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)