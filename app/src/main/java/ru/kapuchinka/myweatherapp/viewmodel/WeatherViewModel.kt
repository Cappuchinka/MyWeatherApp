package ru.kapuchinka.myweatherapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse
import ru.kapuchinka.myweatherapp.repository.WeatherRepository

class WeatherViewModel : ViewModel() {
    private val weatherRepository = WeatherRepository()

    val weatherResponse = mutableStateOf<WeatherResponse?>(null)
    val weatherIcon = mutableStateOf<String?>(null)

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            weatherResponse.value = weatherRepository.getWeatherByCity(city, APPID, UNITS)
            weatherIcon.value = weatherResponse.value?.weather?.get(0)?.icon
        }
    }

    fun getWeatherByLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherResponse.value = weatherRepository.getWeatherByLocation(lat, lon, APPID, UNITS)
            weatherIcon.value = weatherResponse.value?.weather?.get(0)?.icon
        }
    }

    companion object {
        private const val APPID = "c50ba949b50e3b521271fb2b6a25f0e5"
        private const val UNITS = "metric"
    }
}