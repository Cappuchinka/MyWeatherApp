package ru.kapuchinka.myweatherapp.utils.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("weather_table")
data class WeatherModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Int? = null,
    @ColumnInfo("city") val city: String,
    @ColumnInfo("lat") val lat: Double,
    @ColumnInfo("lon") val lon: Double
)
