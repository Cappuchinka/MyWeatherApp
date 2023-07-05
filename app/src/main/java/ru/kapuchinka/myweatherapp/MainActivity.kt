package ru.kapuchinka.myweatherapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
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
            text = "Temp: $temp",
            modifier = modifier
        )
        if (!weatherIconUrl.isNullOrBlank()) {
            WeatherIcon(iconUrl = "https://openweathermap.org/img/w/${weatherIconUrl}.png", size = 128.dp)
        }
    }
}

@Composable
fun WeatherIcon(iconUrl: String, size: Dp) {
    Box(
        modifier = Modifier
            .height(size)
            .width(size),
        contentAlignment = Alignment.Center
    ) {
        val painter = rememberImagePainter(
            data = iconUrl,
            builder = {

            }
        )
        val painterState = painter.state
        Image(painter = painter, contentDescription = "Weather Icon", modifier = Modifier.size(size))
        if (painterState is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}


