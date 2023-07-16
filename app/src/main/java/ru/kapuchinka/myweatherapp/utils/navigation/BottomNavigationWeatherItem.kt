package ru.kapuchinka.myweatherapp.utils.navigation

import ru.kapuchinka.myweatherapp.R

sealed class BottomNavigationWeatherItem(val title: String, val IconId: Int, val route: String) {
    object CurrentLocationWeather: BottomNavigationWeatherItem("Current", R.drawable.current_location, "current")
    object LocationList: BottomNavigationWeatherItem("Locations", R.drawable.list_locations, "locations")
    object FavoritesList: BottomNavigationWeatherItem("Favorites", R.drawable.list_favorite, "favorites")
}
