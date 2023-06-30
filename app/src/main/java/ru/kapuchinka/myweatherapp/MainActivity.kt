package ru.kapuchinka.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
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
                    Column {
                        Greeting("Voronezh", weatherViewModel)
                        WeatherIcon(iconUrl = "https://openweathermap.org/img/w/01d.png", size = 64.dp)
                    }

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
    val tempMax = weatherResponse?.main?.temp_max

    Text(
        text = "Hello $tempMax!",
        modifier = modifier
    )
}

@Composable
fun WeatherIcon(iconUrl: String, size: Dp) {
    Image(
        painter = rememberImagePainter(
            data = iconUrl,
            builder = {
                transformations(CircleCropTransformation())
                // Дополнительные настройки, если необходимо
            }
        ),
        contentDescription = null,
        modifier = Modifier.size(size)
    )
}
