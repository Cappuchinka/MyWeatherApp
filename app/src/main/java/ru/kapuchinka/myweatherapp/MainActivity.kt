package ru.kapuchinka.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import ru.kapuchinka.myweatherapp.ui.theme.MyWeatherAppTheme
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        setContent {
            MyWeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Voronezh", weatherViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(city: String, weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(city) {
        weatherViewModel.getWeatherByCity(city)
    }

    val weatherResponse = weatherViewModel.weatherResponse.value
    val tempMax = weatherResponse?.list?.getOrNull(5)?.main?.temp_max

    Text(
        text = "Hello $tempMax!",
        modifier = modifier
    )
}
