package ru.kapuchinka.myweatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kapuchinka.myweatherapp.utils.db.WeatherDatabase
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel
import ru.kapuchinka.myweatherapp.utils.db.repository.WeatherRepositoryRoom

class WeatherRoomViewModel(application: Application) : AndroidViewModel(application) {
    val allData: LiveData<List<WeatherModel>>
    private val weatherRepositoryRoom: WeatherRepositoryRoom

    init {
        val weatherDao = WeatherDatabase.getDatabase(application).getDao()
        weatherRepositoryRoom = WeatherRepositoryRoom(weatherDao)
        allData = weatherRepositoryRoom.getAll
    }

    fun insertWeather(weatherModel: WeatherModel) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepositoryRoom.insertWeather(weatherModel)
        }
    }
}