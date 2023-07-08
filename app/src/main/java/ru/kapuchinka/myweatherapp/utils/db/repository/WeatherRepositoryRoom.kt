package ru.kapuchinka.myweatherapp.utils.db.repository

import androidx.lifecycle.LiveData
import ru.kapuchinka.myweatherapp.utils.db.dao.WeatherDao
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel

class WeatherRepositoryRoom(private val weatherDao: WeatherDao) {
    val getAll: LiveData<List<WeatherModel>> = weatherDao.getAll()

    suspend fun insertWeather(weatherModel: WeatherModel) {
        weatherDao.insertWeather(weatherModel)
    }

    suspend fun getWeatherById(id: Int) : WeatherModel {
        return weatherDao.getWeatherByID(id)
    }
}