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

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            weatherResponse.value = weatherRepository.getWeatherByCity(city, APPID, UNITS)
        }
    }

    companion object {
        private const val APPID = "c50ba949b50e3b521271fb2b6a25f0e5"
        private const val UNITS = "metric"
    }
}
