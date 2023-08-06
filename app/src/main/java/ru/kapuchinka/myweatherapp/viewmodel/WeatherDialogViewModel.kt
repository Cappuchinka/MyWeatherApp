package ru.kapuchinka.myweatherapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse
import ru.kapuchinka.myweatherapp.repository.WeatherRepositoryRetrofit

class WeatherDialogViewModel : ViewModel() {
    private val weatherRepositoryRetrofit = WeatherRepositoryRetrofit()

    val weatherResponse = mutableStateOf<Response<WeatherResponse>?>(null)
    val weatherIcon = mutableStateOf<String?>(null)

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            weatherResponse.value = weatherRepositoryRetrofit.getWeatherByCity(city, APPID, UNITS)
            weatherIcon.value = weatherResponse.value?.body()?.weather?.get(0)?.icon
        }
    }

    companion object {
        private const val APPID = "c50ba949b50e3b521271fb2b6a25f0e5"
        private const val UNITS = "metric"
    }
}