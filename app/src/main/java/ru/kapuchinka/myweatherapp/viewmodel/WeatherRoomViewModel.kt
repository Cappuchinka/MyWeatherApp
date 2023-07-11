package ru.kapuchinka.myweatherapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
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
    private val weatherRepositoryRoom: WeatherRepositoryRoom
    val weatherById: MutableLiveData<WeatherModel> = MutableLiveData()

    init {
        val weatherDao = WeatherDatabase.getDatabase(application).getDao()
        weatherRepositoryRoom = WeatherRepositoryRoom(weatherDao)
        allData = weatherRepositoryRoom.getAll
    }

    fun insertWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepositoryRoom.insertWeather(WeatherModel(null, city))
        }
    }

    fun getWeatherById(id: Int){
        viewModelScope.launch {
            weatherById.value = weatherRepositoryRoom.getWeatherById(id)
            Log.d("DB", weatherById.value!!.city)
        }
    }
}