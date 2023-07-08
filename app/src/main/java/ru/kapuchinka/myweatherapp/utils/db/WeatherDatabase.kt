package ru.kapuchinka.myweatherapp.utils.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kapuchinka.myweatherapp.utils.db.dao.WeatherDao
import ru.kapuchinka.myweatherapp.utils.db.model.WeatherModel

@Database(entities = [WeatherModel::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getDao() : WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context) : WeatherDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}