package ru.kapuchinka.myweatherapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kapuchinka.myweatherapp.utils.db.WeatherDatabase
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel
import ru.kapuchinka.myweatherapp.utils.db.repository.WeatherRepositoryRoom

class WeatherRoomViewModel(application: Application) : AndroidViewModel(application) {
    val allData: LiveData<List<WeatherModel>>
    val favoritesLocations: LiveData<List<WeatherModel>>
    private val weatherRepositoryRoom: WeatherRepositoryRoom
    val weatherByName: MutableLiveData<WeatherModel> = MutableLiveData()

    init {
        val weatherDao = WeatherDatabase.getDatabase(application).getDao()
        weatherRepositoryRoom = WeatherRepositoryRoom(weatherDao)
        allData = weatherRepositoryRoom.getAll
        favoritesLocations = weatherRepositoryRoom.favoritesLocations
    }

    fun insertWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepositoryRoom.insertWeather(WeatherModel(null, city, false))
        }
    }

    fun getWeatherByName(city: String) {
        viewModelScope.launch {
            weatherByName.value = weatherRepositoryRoom.getWeatherByName(city)
            Log.d("DB", weatherByName.value!!.city)
        }
    }

    fun updateCity(isFavorite: Boolean, city: String) {
        viewModelScope.launch {
            weatherRepositoryRoom.updateCity(isFavorite, city)
        }
    }

    fun deleteCity(city: String) {
        viewModelScope.launch {
            weatherRepositoryRoom.deleteCity(city)
        }
    }
}