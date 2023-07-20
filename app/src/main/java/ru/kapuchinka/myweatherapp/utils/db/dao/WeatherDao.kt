package ru.kapuchinka.myweatherapp.utils.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM weather_table WHERE city = :city")
    suspend fun getWeatherByName(city: String): WeatherModel

    @Query("SELECT * FROM weather_table WHERE is_favorite = :isFavorite ORDER BY city ASC")
    fun getFavoritesLocation(isFavorite: Boolean): LiveData<List<WeatherModel>>

    @Query("UPDATE weather_table SET is_favorite = :isFavorite WHERE city = :city")
    suspend fun updateCity(isFavorite: Boolean, city: String)

    @Query("DELETE FROM weather_table WHERE city = :city")
    suspend fun deleteCity(city: String)
}