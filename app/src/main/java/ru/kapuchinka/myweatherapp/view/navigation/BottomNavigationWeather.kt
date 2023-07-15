package ru.kapuchinka.myweatherapp.view.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.kapuchinka.myweatherapp.utils.navigation.BottomNavigationWeatherItem

@Composable
fun BottomNavigationWeather(
    navController: NavController
) {
    val listItems = listOf(
        BottomNavigationWeatherItem.CurrentLocationWeather,
        BottomNavigationWeatherItem.LocationList,
        BottomNavigationWeatherItem.FavoritesList
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        listItems.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Box(modifier = Modifier.size(28.dp)) {
                        Icon(
                            painter = painterResource(id = item.IconId),
                            contentDescription = "icon",
                        )
                    }
                },
                label = {
                    Text(text = item.title, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                },
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}