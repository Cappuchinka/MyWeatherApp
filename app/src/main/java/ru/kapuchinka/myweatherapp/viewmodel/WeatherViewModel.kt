package ru.kapuchinka.myweatherapp.viewmodel

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import ru.kapuchinka.myweatherapp.api.model.WeatherResponse
import ru.kapuchinka.myweatherapp.repository.WeatherRepositoryRetrofit

class WeatherViewModel : ViewModel() {
    private val weatherRepositoryRetrofit = WeatherRepositoryRetrofit()

    val weatherResponse = mutableStateOf<WeatherResponse?>(null)
    val weatherIcon = mutableStateOf<String?>(null)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @SuppressLint("StaticFieldLeak")
    private lateinit var appContext: Context

    fun setContext(context: Context) {
        appContext = context
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
    }

    @SuppressLint("MissingPermission")
    fun getWeatherByCurrentLocation(context: Context) {
        if (checkPermissions(context)) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        getWeatherByLocation(lat, lon)
                    } else {
                        // Handle the case when location is null
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any exceptions that occurred while getting location
                }
        }
    }

    private fun checkPermissions(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            weatherResponse.value = weatherRepositoryRetrofit.getWeatherByCity(city, APPID, UNITS)
            weatherIcon.value = weatherResponse.value?.weather?.get(0)?.icon
        }
    }

    private fun getWeatherByLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherResponse.value = weatherRepositoryRetrofit.getWeatherByLocation(lat, lon, APPID, UNITS)
            Log.d("RESPONSE", weatherResponse.value!!.name)
            weatherIcon.value = weatherResponse.value?.weather?.get(0)?.icon
            Log.d("RESPONSE", weatherIcon.value!!)
        }
    }

    companion object {
        private const val APPID = "c50ba949b50e3b521271fb2b6a25f0e5"
        private const val UNITS = "metric"
    }
}
