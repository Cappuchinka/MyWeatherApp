package ru.kapuchinka.myweatherapp.repository

import ru.kapuchinka.myweatherapp.api.RetrofitInstance
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse

class WeatherRepository {
    suspend fun getWeatherByCity(city: String, apiKey: String, cnt: Int, units: String) : WeatherResponse {
        return RetrofitInstance.weatherApi.getWeatherByCity(units, apiKey, cnt, city)
    }
}