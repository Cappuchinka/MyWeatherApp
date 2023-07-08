package ru.kapuchinka.myweatherapp.utils.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(weatherModel: WeatherModel)

    @Query("SELECT * FROM weather_table ORDER BY city ASC")
    fun getAll(): LiveData<List<WeatherModel>>

    @Query("SELECT * FROM weather_table WHERE id = :id")
    suspend fun getWeatherByID(id: Int) : WeatherModel
}