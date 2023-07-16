package ru.kapuchinka.myweatherapp.view.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.kapuchinka.myweatherapp.view.weather.WeatherCurrentLocationScreen
import ru.kapuchinka.myweatherapp.view.weathers_list.FavoritesList
import ru.kapuchinka.myweatherapp.view.weathers_list.LocationsList
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

@Composable
fun NavGraph(
    navHostController: NavHostController,
    weatherViewModel: WeatherViewModel,
    weatherRoomViewModel: WeatherRoomViewModel,
    context: Context
) {
    NavHost(navController = navHostController, startDestination = "current") {
        composable("current") {
            WeatherCurrentLocationScreen(
                weatherViewModel = weatherViewModel,
                context = context
            )
        }
        composable("locations") {
            LocationsList(
                weatherRoomViewModel = weatherRoomViewModel,
                weatherViewModel = weatherViewModel,
                context = context
            )
        }
        composable("favorites") {
            FavoritesList(
                weatherRoomViewModel = weatherRoomViewModel,
                weatherViewModel = weatherViewModel,
                context = context
            )
        }
    }
}