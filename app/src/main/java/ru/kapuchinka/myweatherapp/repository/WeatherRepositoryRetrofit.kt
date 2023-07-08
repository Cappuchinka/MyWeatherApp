package ru.kapuchinka.myweatherapp.repository

import ru.kapuchinka.myweatherapp.api.RetrofitInstance
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse

class WeatherRepositoryRetrofit {
    suspend fun getWeatherByCity(city: String, apiKey: String, units: String) : WeatherResponse {
        return RetrofitInstance.weatherApi.getWeatherByCity(units, apiKey,city)
    }

    suspend fun getWeatherByLocation(lat: Double, lon: Double, apiKey: String, units: String) : WeatherResponse {
        return RetrofitInstance.weatherApi.getWeatherByLocation(lat, lon, units, apiKey)
    }
}