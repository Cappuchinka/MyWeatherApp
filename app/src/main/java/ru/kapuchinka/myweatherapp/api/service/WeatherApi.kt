package ru.kapuchinka.myweatherapp.api.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse

interface WeatherApi {
    @GET("forecast")
    suspend fun getWeatherByCity( @Query("units") units: String,
                                  @Query("appid") apiKey: String,
                                  @Query("cnt") count: Int,
                                  @Query("q") location: String): WeatherResponse
}