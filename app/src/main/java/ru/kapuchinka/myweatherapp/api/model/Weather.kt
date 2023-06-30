package ru.kapuchinka.myweatherapp.api.model

data class Weather(
    val dt: Long,
    val main: MainData,
    val weather: List<WeatherInfo>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: Sys,
    val dt_txt: String
)