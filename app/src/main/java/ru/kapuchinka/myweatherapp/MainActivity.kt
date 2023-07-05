package ru.kapuchinka.myweatherapp

import android.os.Bundle
import android.Manifest
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.kapuchinka.myweatherapp.ui.theme.MyWeatherAppTheme
import ru.kapuchinka.myweatherapp.utils.permission.RequestPermission
import ru.kapuchinka.myweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val context: Context = this

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel.setContext(this)

        setContent {
            MyWeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION)
                    Column {
//                        GetWeatherByCity("Moscow", weatherViewModel)
                        GetWeatherByCurrentLocation(context = context, weatherViewModel = weatherViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun GetWeatherByCity(city: String, weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(city) {
        weatherViewModel.getWeatherByCity(city)
    }

    val weatherResponse = weatherViewModel.weatherResponse.value
    val temp = weatherResponse?.main?.temp
    val weatherIconUrl = weatherViewModel.weatherIcon.value

    Column {
        Text(
            text = "Hello $temp!",
            modifier = modifier
        )
        if (!weatherIconUrl.isNullOrBlank()) {
            WeatherIcon(iconUrl = "https://openweathermap.org/img/w/${weatherIconUrl}.png", size = 64.dp)
        }
    }
}

@Composable
fun GetWeatherByCurrentLocation(context: Context, weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(context) {
        weatherViewModel.getWeatherByCurrentLocation(context)
    }

    val weatherResponse = weatherViewModel.weatherResponse.value
    val temp = weatherResponse?.main?.temp
    val weatherIconUrl = weatherViewModel.weatherIcon.value

    Column {
        Text(
            text = "Temp: $temp!",
            modifier = modifier
        )
        if (!weatherIconUrl.isNullOrBlank()) {
            WeatherIcon(iconUrl = "https://openweathermap.org/img/w/${weatherIconUrl}.png", size = 64.dp)
        }
    }
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


