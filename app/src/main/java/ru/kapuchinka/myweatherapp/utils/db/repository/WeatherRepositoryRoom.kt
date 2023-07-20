package ru.kapuchinka.myweatherapp.utils.db.repository

import androidx.lifecycle.LiveData
import ru.kapuchinka.myweatherapp.utils.db.dao.WeatherDao
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel

class WeatherRepositoryRoom(private val weatherDao: WeatherDao) {
    val getAll: LiveData<List<WeatherModel>> = weatherDao.getAll()
    val favoritesLocations: LiveData<List<WeatherModel>> = weatherDao.getFavoritesLocation(true)

    suspend fun insertWeather(weatherModel: WeatherModel) {
        weatherDao.insertWeather(weatherModel)
    }

    suspend fun getWeatherByName(city: String): WeatherModel {
        return weatherDao.getWeatherByName(city)
    }

    suspend fun updateCity(isFavorite: Boolean, city: String) {
        return weatherDao.updateCity(isFavorite, city)
    }

    suspend fun deleteCity(city: String) {
        return weatherDao.deleteCity(city)
    }
}