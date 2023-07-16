package ru.kapuchinka.myweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ru.kapuchinka.myweatherapp.ui.theme.MyMaterialTheme
import ru.kapuchinka.myweatherapp.view.navigation.BottomNavigationWeather
import ru.kapuchinka.myweatherapp.view.navigation.NavGraph
import ru.kapuchinka.myweatherapp.viewmodel.WeatherRoomViewModel
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val weatherRoomViewModel: WeatherRoomViewModel by viewModels()
    private val context: Context = this

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel.setContext(this)

        setContent {
            MyMaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigationWeather(navController = navController)
                        }
                    ) {
                        NavGraph(
                            navHostController = navController,
                            weatherViewModel = weatherViewModel,
                            weatherRoomViewModel = weatherRoomViewModel,
                            context = context
                        )
                    }
                }
            }
        }
    }
}
