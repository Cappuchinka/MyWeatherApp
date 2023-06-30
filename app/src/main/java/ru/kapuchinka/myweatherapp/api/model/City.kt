package ru.kapuchinka.myweatherapp.api.model

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)